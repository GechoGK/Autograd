package nnet.layers;

import nnet.*;
import nnet.math.*;

public class Dropout extends Module
{
	private float dropout;

	public Dropout(float dropout)
	{
		this.dropout = dropout;
	}
	@Override
	public VMat forward(VMat input)
	{
		if (input instanceof VMat3)
		{
			throw new RuntimeException("vmat3 not implemented for now. plase use 2d matrix(VMat)");	
		}
		else if (input instanceof VMat)
		{
			VMat mask=VMat.randB(input.row, input.col, dropout);
			VMat rs=VMat.mul(mask, input);
			if (dropout == 1)
				return rs;
			VMat rs2=VMat.div(rs, 1 - dropout);
			return rs2;
		}
		return null;
	}
	@Override
	public String toString()
	{
		return "dropout layer(" + dropout + ")" ;
	}
}
