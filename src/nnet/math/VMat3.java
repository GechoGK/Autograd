package nnet.math;

public class VMat3 extends VMat
{
	public VMat[] data;
	public int depth=0;

	public VMat3()
	{}
	public VMat3(int d, int r, int c)
	{
		this.depth = d;
		data = new VMat[d];
		for (int i=0;i < d;i++)
			data[i] = new VMat(r, c);
	}
	public Value get(int d, int r, int c)
	{
		return data[d].get(r, c);
	}
	public void put(int d, int r, int c, Value v)
	{
		data[d].put(r, c, v);
	}
	public void put(int d, int r, int c, float v)
	{
		data[d].put(r, c, v);
	}
	public VMat getAt(int r)
	{
		return data[r];
	}
}
