package nnet.optimizers;

import java.util.*;
import nnet.*;
import nnet.math.*;
import nnet.loss.*;

public class GradientDescent extends Optimizer
{
	private Module module;

	// private static HashSet<Value> values=new HashSet<>();

//	public static boolean has(Value v)
//	{
//		if (values.contains(v))
//			return true;
//		values.add(v);
//		return false;
//	}

	public GradientDescent()
	{
		loss = new MSE();
	}
	public GradientDescent(Module m)
	{
		this();
		this.module = m;
	}
	public GradientDescent(Module m, LossFunction loss)
	{
		this.module = m;
		this.loss = loss;
	}
	public GradientDescent(LossFunction loss)
	{
		this.loss = loss;
	}
	public GradientDescent(LossFunction loss, float learningRate)
	{
		this.loss = loss;
		this.lr = learningRate;
	}
	public GradientDescent(Module m, LossFunction loss, float learningRate)
	{
		this.module = m;
		this.loss = loss;
		this.lr = learningRate;
	}
	/*
	 gradient_descent(model, X, y, learning_rate, num_iterations):
	 """
	 Optimizes model parameters using gradient descent.

	 Parameters:
	 model (object): The machine learning model to be optimized.
	 X (array-like): The input data for the model.
	 y (array-like): The target values for the model.
	 learning_rate (float): The learning rate for the optimizer.
	 num_iterations (int): The number of iterations for the optimizer.

	 Returns:
	 object: The optimized model.
	 """
	 # obtain the number of training examples
	 m = X.shape[0]

	 for i in range(num_iterations):
	 # make predictions using the current model parameters
	 y_pred = model.predict(X)

	 # calculate gradients
	 grads = model.gradient(X, y, y_pred)

	 # update model parameters
	 for j in range(len(model.params)):
	 model.params[j] = model.params[j] - learning_rate * grads[j] / m

	 return model
	 */
	public void train(VMat data, VMat tar)
	{
		if (module == null)
			throw new RuntimeException("no module found to tain");
		if (loss == null)
			throw new RuntimeException("no loss function found to calculate error");
		train(module, data, tar);
	}
	@Override
	public void train(Module m, VMat data, VMat tar)
	{
		// predict.
		// for (int i=0;i < iter;i++)
		// {
		VMat prd=m.forward(data);
		// calculate error
		VMat error=loss.forward(prd, tar);
		setResult(prd, error.get(0, 0).data);
		// System.out.println(error);
		// calculate gradient throught error
		error.backward(1);
		// update model weight based on calculated gradient.
		calculateGradient(m.getParameters());
		// }
	}
	private void calculateGradient(List<Mat> params)
	{
		// System.out.println("total values =" + values.size());
		for (Mat m:params)
		{
			if (m instanceof VMat3)
				calcMat3((VMat3)m);
			else if (m instanceof VMat)
				calcVMat((VMat)m);
		}
	}
	private void calcVMat(VMat vm)
	{
		// System.out.println("calulating matrix 2d");
		for (int r=0;r < vm.row;r++)
			for (int c=0;c < vm.col;c++)
			{
				Value v=vm.get(r, c);
				double gr=v.grad * lr;
				if (Double.isNaN(gr))
					return;
				if (Double.isInfinite(gr))
					return;

				v.data = v.data - gr;
			}
	}
	private void calcMat3(VMat3 vm)
	{
		// System.out.println("calulating matrix 3d");
		for (int i=0;i < vm.depth;i++)
		{
			calcVMat(vm.getAt(i));
		}
	}
	public void zeroGrad()
	{
		// values.clear();
		List<Mat> prm=module.getParameters();
		for (Mat m:prm)
		{
			if (m instanceof VMat3)
				zeroMat3((VMat3)m);
			else if (m instanceof VMat)
				zeroMat((VMat)m);
		}
	}
	private void zeroMat(VMat vm)
	{
		// System.out.println("zero matrix 2d");
		for (int r=0;r < vm.row;r++)
			for (int c=0;c < vm.col;c++)
			{
				Value v=vm.get(r, c);
				v.grad = 0;
			}
	}
	private void zeroMat3(VMat3 vm)
	{
		// System.out.println("zero matrix 3d");
		for (int i=0;i < vm.depth;i++)
		{
			calcVMat(vm.getAt(i));
		}
	}
}
