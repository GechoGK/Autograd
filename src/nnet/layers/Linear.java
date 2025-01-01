package nnet.layers;

import nnet.*;
import nnet.math.*;

public class Linear extends Module
{
	public VMat weight;
	public VMat biase;
	private boolean hasBiase;
	private int inSize,outSize;

	public Linear(int in, int out)
	{
		this(in, out, true);
	}
	public Linear(int in, int out, boolean hasBiase)
	{
		this.hasBiase = hasBiase;
		init(in, out);
	}
	private void init(int in, int out)
	{
		this.inSize = in;
		this.outSize = out;
		weight = newParam(VMat.rand(in, out));
		if (hasBiase)
			biase = newParam(VMat.rand(1, out));
	}
	@Override
	public VMat forward(VMat input)
	{
		if (hasBiase)
		{
			VMat dt=VMat.dot(input, weight);
			return VMat.add(dt, biase);
		}

		return VMat.dot(input, weight);
	}
	@Override
	public String toString()
	{
		return "linear layer(" + inSize + ", " + outSize + ") has Biase ? =" + hasBiase + ".";
	}
}
