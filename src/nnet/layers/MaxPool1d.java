package nnet.layers;

import java.util.*;
import nnet.*;
import nnet.math.*;

public class MaxPool1d extends Module
{
	private int poolSize;

	public MaxPool1d(int poolSize)
	{
		this.poolSize = poolSize;
	}
	@Override
	public VMat forward(VMat in)
	{
		VMat rs=new VMat(in.row, in.col / poolSize);
		for (int i=0;i < in.row;i++)
			for (int j=0;j < in.col;j += poolSize)
			{
				int idx=maxIndex(in.data[i], j, poolSize);
				rs.put(i, j / poolSize, in.data[i][idx]);
			}
		return rs;
	}
	public int maxIndex(Value[] ar, int str, int len)
	{
		double v=0;
		int idx=0;
		for (int i=str;i < str + len;i++)
			if (ar[i].data >= v)
			{
				idx = i;
				v = ar[i].data;
			}
		return idx;
	}
	@Override
	public String toString()
	{
		return "MaxPool1D(pool size =" + poolSize + ")";
	}

}
