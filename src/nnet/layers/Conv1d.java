package nnet.layers;

import nnet.*;
import nnet.math.*;

import nnet.Module;

public class Conv1d extends Module
{
	public VMat3 filters;
	public VMat biase;

	private int n_channels;
	private int n_kernels;
	private int kernel_size;
	private int stride=0;
	private int padding=0;
	private int outputSize=0;
	private int inputSize=0;

	/*
	 not implemented.
	 */

	public Conv1d(int n_channels, int n_kernels, int kernel_size, int stride, int padding)
	{
		this.n_channels = n_channels;
		this.n_kernels = n_kernels;
		this.kernel_size = kernel_size;
		this.stride = stride;
		this.padding = padding;
		if (stride != 0 || padding != 0)
			throw new RuntimeException("paddings and strides are not implemented, atleast for now, please don't use them until they are implemented.");
		// initialize.
	}
	public Conv1d(int in_channels, int out_features, int kernel_size, int stride)
	{
		this(in_channels, out_features, kernel_size, stride, 0);
	}
	public Conv1d(int in_channels, int out_features, int kernel_size)
	{
		this(in_channels, out_features, kernel_size, 0, 0);
	}
	private void initialize(VMat in)
	{
		/*
		 filters 3D array.
		 1. channels.
		 2. kernels.
		 3. kernels sise.
		 filter=(n_channels,n_kernels,kernrlSize);
		 at the end the filter.
		 all channels sum up to create 1 channel for output.
		 output = 2D array.
		 ----
		 biase 2D array.
		 biase =(n_kernels,kernelSize);
		 */
		inputSize = in.col;
		outputSize = inputSize - kernel_size + 1;
		filters = newParam(new VMat3(n_kernels, n_channels, kernel_size));
		for (int i=0;i < n_kernels;i++)
			for (int j=0;j < n_channels;j++)
				for (int k=0;k < kernel_size;k++)
					filters.put(i, j, k, (float)Math.random());
		biase = newParam(VMat.ones(n_kernels, outputSize));
		// System.out.println("init filter(" + filters.length + ", " + filters[0].length + ", " + filters[0][0].length + ")");
		// System.out.println("init biase (" + biase.length + ", " + biase[0].length + ")");
	}
	@Override
	public VMat forward(VMat in)
	{
		// System.out.println("=== " + (input.col - kernel_size));
		if (filters == null)
			initialize(in);
		VMat output=calculateOutput(in, filters);
		// forward pass completed successfully.
		// TO-DO return ndimArray;
		return output;
	}
	public VMat calculateOutput(VMat in, VMat3 kerns)//(float[][] in, float[][][] kerns)
	{
		/*
		 iterate through all channels.
		 for each and every channel calculate their convolution.

		 in = 2D array (n_channels,inputSize);
		 filters = 3D array (n_channels,n_kernels,kernelSize)
		 output=2D array (n_kernels, outputSize);
		 final output= output + biase;
		 */
		if (in.row != n_channels)
			throw new RuntimeException("number of channels expected for input is =" + n_channels + ", found =" + in.row);
		VMat output=new VMat(n_kernels, outputSize);
		// for (int c=0;c < n_channels;c++) // # number of channels
		// {
		for (int k=0;k < n_kernels;k++)
		{
			VMat out=VMat.convolve(in, kerns.getAt(k));
			output.put(k, out.data[0]);
			// Matrix2.conv1d(in, kerns[c][k], output[k]);
		}
		// }
		// System.out.println(Matrix2.asString(output));
		VMat result=VMat.add(output, biase);
		// final outputSize = (n_kernels, outputSize);
		// System.out.println(Matrix2.asString(output));
		// System.out.println("(" + output.length + ", " + output[0].length + ") == (" + biase.length + ", " + biase[0].length + ")");
		return result;
	}
	@Override
	public String toString()
	{
		// TODO: Implement this method
		return "convolution module(num_channels=" + n_channels + ", num_kernels=" + n_kernels + ", kern_size=" + kernel_size + ")";
	}
}
