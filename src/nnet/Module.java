package nnet;

import java.util.*;
import nnet.math.*;

public class Module
{
	protected List<Mat> params=new ArrayList<>();

	public VMat forward(VMat input)
	{
		return null;
	}
	public List<Mat> getParameters()
	{
		return params;
	}
	public <T extends Mat>T newParam(T v)
	{
		if (!params.contains(v))
			params.add(v);
		return v;
	}
	@Override
	public String toString()
	{
		return "module (" + ")";
	}
}
