package nnet.layers;

import nnet.*;
import nnet.math.*;

public class Flatten extends Module
{
	public Flatten()
	{

	}
	@Override
	public VMat forward(VMat input)
	{
		if (input instanceof VMat3)
		{
			throw new RuntimeException("vmat3 not implemented.");
		}
		else if (input instanceof VMat)
		{
			if (input.row == 1)
				return input;
			VMat vm=new VMat(1, input.row * input.col);
			int idx=0;
			for (int r=0;r < input.row;r++)
				for (int c=0;c < input.col;c++)
				{
					vm.put(0, idx, input.get(r, c));
					idx++;
				}
			return vm;
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "flatten layer()";
	}

}
