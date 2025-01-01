package nnet.loss;

import nnet.*;
import nnet.math.*;

public class BCrossEntropy extends LossFunction
{
	/*
	 def binary_cross_entropy(actual, predicted):
	 sum_score = 0.0
	 for i in range(len(actual)):
	 sum_score += actual[i] * log(1e-15 + predicted[i])
	 mean_sum_score = 1.0 / len(actual) * sum_score
	 return -mean_sum_score
	 */
	@Override
	public VMat forward(VMat predicted, VMat actual)
	{
		// TODO: Implement this method
		VMat prd=VMat.value(predicted.row, predicted.col, 1e-15);
		prd = VMat.add(predicted, prd);
		prd = VMat.log(prd);
		prd = VMat.mul(actual, prd);
		VMat sum=VMat.sum(prd);

		sum = VMat.mul(sum, (1.0 / predicted.data.length));

		return sum;
	}
}
