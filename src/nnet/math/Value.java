package nnet.math;

import nnet.optimizers.*;

public class Value
{
	public double data=0,grad=0;
	public Value[] childs;
	public VMat owner;

	public void backward()
	{
		for (Value v:childs)
			v.backward();
	}
	public void backward(double grad)
	{
		this.grad = grad;
		backward();
	}
	public void backward(Value v)
	{
		this.grad = v.data;
		backward();
	}
	public Value get(int p)
	{
		if (p >= childs.length)
			throw new RuntimeException("unable to access child at " + p);
		return childs[p];
	}
	public Value(VMat vm)
	{
		this.owner = vm;
		this.data = 0;
	}
	public Value(double v, Value...chlds)
	{
		this.data = v;
		this.childs = chlds;
	}
	public Value set(double d, Value...chld)
	{
		this.data = d;
		this.childs = chld;
		return this;
	}
	public void setGrad(double val)
	{
		this.grad += val;
	}
	@Override
	public String toString()
	{
		return "value {data =" + data + " , grad =" + grad + "}";
	}
	public static Value add(Value op1, Value op2)
	{
		Value v=new Value(op1.data + op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].setGrad(this.grad);
				this.childs[1].setGrad(this.grad);
				super.backward();
			}
		};
		return v;
	}
	public static Value mul(Value op1, Value op2)
	{
		Value v=new Value(op1.data * op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].setGrad(this.grad * this.childs[1].data);
				this.childs[1].setGrad(this.grad * this.childs[0].data);
				super.backward();
			}
		};
		return v;
	}
	public static Value sub(Value op1, Value op2)
	{
		Value v=new Value(op1.data - op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].setGrad(this.grad);
				this.childs[1].setGrad(-this.grad);
				super.backward();
			}
		};
		return v;
	}
	public static Value div(Value op1, Value op2)
	{
		Value v=new Value(op1.data / op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].setGrad(this.grad * 1 / this.childs[1].data);
				this.childs[1].setGrad(-this.grad * this.childs[0].data / (childs[1].data * childs[1].data));
				super.backward();
				// -(grad * getArg(0) / (getArg(1) * getArg(1));
			}
		};
		return v;
	}
	public static Value div(Value op1, final double val)
	{
		Value v=new Value(op1.data / val, op1){
			public void backward()
			{
				this.childs[0].setGrad(this.grad * 1 / val);// this.childs[1].data;
				// this.childs[1].setGrad( -this.grad * this.childs[0].data / (childs[1].data * childs[1].data);
				super.backward();
				// -(grad * getArg(0) / (getArg(1) * getArg(1));
			}
		};
		return v;
	}
	public static Value log(Value op1)
	{
		// it doesn't works at all
		// dont't use it.
		// it doesn't work as expected.
		Value v=new Value(Math.log10(op1.data), op1){
			// if { inputs.get(0).backward(grad * (1 / (Math.log(10) * getArg(0))));
			public void backward()
			{
				this.childs[0].setGrad(1 / this.grad);
				// this.childs[0].setGrad(this.grad * this.childs[1].data * Math.pow(this.childs[0].data, childs[1].data - 1));
				super.backward();
			}
		};
		return v;
	}
	public static Value pow(Value op1, Value op2)
	{
		Value v=new Value(Math.pow(op1.data , op2.data), op1, op2){
			// inputs.get(0).backward(grad * getArg(1) * Math.pow(getArg(0), getArg(1) - 1));
			// inputs.get(1).backward(grad * (Math.pow(getArg(0), getArg(1)) * Math.log(getArg(0))));
			public void backward()
			{
				this.childs[0].setGrad(this.grad * this.childs[1].data * Math.pow(this.childs[0].data, childs[1].data - 1));
				this.childs[1].setGrad(this.grad * (Math.pow(this.childs[0].data, this.childs[1].data) * Math.log(childs[0].data)));
				super.backward();
			}
		};
		return v;
	}
	public static Value pow(Value op1, final double val)
	{
		Value v=new Value((float)Math.pow(op1.data , val), op1){
			// inputs.get(0).backward(grad * getArg(1) * Math.pow(getArg(0), getArg(1) - 1));
			public void backward()
			{
				this.childs[0].setGrad(this.grad * val * Math.pow(this.childs[0].data, val - 1));
				super.backward();
			}
		};
		return v;
	}
	public static Value sigmoid(Value op1)
	{
		/*
		 def sigmoid(self, x):
		 return 1 / (1 + np.exp(-x))

		 def sigmoid_derivative(self, x):
		 return x * (1 - x)
		 */
		double out = 1 / (1 + Math.exp(-op1.data));
		Value v=new Value(out, op1){
			@Override
			public void backward()
			{
				this.childs[0].setGrad(this.grad * (1 - this.grad));
				super.backward();
			}
		};
		return v;
	}
	public static Value tanh(Value op1)
	{
		/*
		 def tanh(self, x):
		 return Math.tanh(x);
		 def tanh_derivative(self, x):
		 return 1 - x ^ 2;
		 */
		double out = Math.tanh(op1.data);
		Value v=new Value(out, op1){
			@Override
			public void backward()
			{
				this.childs[0].setGrad(1 - this.grad * this.grad);
				super.backward();
			}
		};
		return v;
	}
	public static Value relu(Value op1)
	{
		/*
		 def relu(self, x):
		 return Math.max(0,x);
		 def relu_derivative(self, x):
		 return x<0?0:1;
		 */
		double out = Math.max(0, op1.data);
		Value v=new Value(out, op1){
			@Override
			public void backward()
			{
				this.childs[0].setGrad(this.grad < 0 ? 0 : 1);
				super.backward();
			}
		};
		return v;
	}
}
