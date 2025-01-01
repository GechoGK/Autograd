package nnet.activation;

import nnet.*;
import nnet.math.*;

public class Sigmoid extends Module
{
	@Override
	public VMat forward(VMat input)
	{
		return VMat.sigmoid(input);
	}
}
