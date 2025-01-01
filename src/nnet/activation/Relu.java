package nnet.activation;

import nnet.*;
import nnet.math.*;

public class Relu extends Module
{
	@Override
	public VMat forward(VMat input)
	{
		return VMat.relu(input);
	}
}
