package nnet.activation;

import nnet.*;
import nnet.math.*;

public class Tanh extends Module
{
	@Override
	public VMat forward(VMat input)
	{
		return VMat.tanh(input);
	}
}
