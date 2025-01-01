package nnet.loss;

import nnet.*;
import nnet.math.*;

public class MSE extends LossFunction
{
	@Override
	public VMat forward(VMat predicted, VMat actual)
	{
		if (predicted.data.length != actual.data.length)
			throw new RuntimeException("different array size occured..");
		// System.out.println("predicted =" + predicted);
		// System.out.println("calculating error");
		VMat square_diff=VMat.pow(VMat.sub(predicted, actual), 2);
		// System.out.println("squareDiff =" + square_diff);
		VMat mse=VMat.div(VMat.sum(square_diff, true), square_diff.col);
		// System.out.println("summed =" + mse);
		// if (mse.get(0, 0).data > 1000)
		// 	mse.put(0, 0, 100);
		return mse;
	}

}
