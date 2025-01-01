package raw;

import static raw.ValuePool.*;

public class VMat
{
	public int row,col;
	public Value[][] data;

	public VMat(int r, int c)
	{
		this.row = r;
		this.col = c;
		this.data = new Value[r][c];
	}
	public VMat(int r, int c, float[][] d)
	{
		this(r, c);
		for (int i=0;i < r;i++)
			for (int j=0;j < c;j++)
				this.data[i][j] = newValue(d[i][j]);
	}
	public VMat(float[][] d)
	{
		if (d.length == 0)
		{
			row = 0;
			col = 0;
			this.data = new Value[0][0];
			return;
		}
		this.row = d.length;
		this.col = d[0].length;
		this.data = new Value[row][col];
		for (int i=0;i < row;i++)
			for (int j=0;j < col;j++)
			{
				this.data[i][j] = newValue(d[i][j]);
			}
	}
	public VMat(Value[][] data)
	{
		this .row = data.length;
		this.col = data[0].length;
		this.data = data;
	}
	public VMat(float[] data)
	{
		this(new float[][]{data});
	}
	public Value get(int r, int c, float def)
	{
		if (r >= row || c >= col)
			return newValue(def);
		return data[r][c];
	}
	public Value get(int r, int c)
	{
		if (r >= row || c >= col)
			throw new RuntimeException("value not found");
		return data[r][c];
	}
	public void put(int r, int c, Value v)
	{
		data[r][c] = v;
	}
	public void put(int r, Value[] v)
	{
		data[r] = v;
	}
	public Value[] get(int r)
	{
		return data[r];
	}
	public static VMat add(VMat op1, VMat op2)
	{
		VMat vm=new VMat(Math.max(op1.row, op2.row), Math.max(op1.col, op2.col));
		for (int r=0;r < Math.max(op1.row, op2.row);r++)
			for (int c=0;c < Math.max(op1.col, op2.col);c++)
				vm.put(r, c, Value.add(op1.get(r, c, 0), op2.get(r, c, 0)));
		return vm;
	}
	public static VMat sub(VMat op1, VMat op2)
	{
		VMat vm=new VMat(Math.max(op1.row, op2.row), Math.max(op1.col, op2.col));
		for (int r=0;r < Math.max(op1.row, op2.row);r++)
			for (int c=0;c < Math.max(op1.col, op2.col);c++)
				vm.put(r, c, Value.sub(op1.get(r, c), op2.get(r, c)));
		return vm;
	}
	public static VMat mul(VMat op1, VMat op2)
	{
		VMat vm=new VMat(Math.max(op1.row, op2.row), Math.max(op1.col, op2.col));
		for (int r=0;r < Math.max(op1.row, op2.row);r++)
			for (int c=0;c < Math.max(op1.col, op2.col);c++)
				vm.put(r, c, Value.mul(op1.get(r, c), op2.get(r, c)));
		return vm;
	}
	public static VMat div(VMat op1, VMat op2)
	{
		VMat vm=new VMat(Math.max(op1.row, op2.row), Math.max(op1.col, op2.col));
		for (int r=0;r < Math.max(op1.row, op2.row);r++)
			for (int c=0;c < Math.max(op1.col, op2.col);c++)
				vm.put(r, c, Value.div(op1.get(r, c), op2.get(r, c)));
		return vm;
	}
	public static VMat pow(VMat op1, VMat op2)
	{
		VMat vm=new VMat(Math.max(op1.row, op2.row), Math.max(op1.col, op2.col));
		for (int r=0;r < Math.max(op1.row, op2.row);r++)
			for (int c=0;c < Math.max(op1.col, op2.col);c++)
				vm.put(r, c, Value.pow(op1.get(r, c), op2.get(r, c)));
		return vm;
	}
	public static VMat sum(VMat op1)
	{
		VMat vm=new VMat(1, 1);
		Value v=newValue(0);
		for (int r=0;r < op1.row;r++)
			for (int c=0;c < op1.col;c++)
				v = Value.add(v, op1.get(r, c));
		vm.put(0, 0, v);
		return vm;
	}
	public static VMat dot(VMat x, VMat y)
	{
		if (x.col != y.row)
			throw new RuntimeException("two matrixes doesn't match (" + x.col + "," + y.row + ")");
		VMat m=new VMat(x.row, y.col);
		for (int r=0;r < x.row;r++)
			for (int c=0;c < y.col;c++)
			{
				Value sum=newValue(0);
				for (int i=0;i < x.col;i++)
					sum = Value.add(sum, Value.mul(x.get(r, i) , y.get(i, c)));
				m.put(r, c, sum);
			}
		return m;
	}
	public static VMat convolve(VMat in, VMat k)
	{
		Value[][] vo=new Value[k.row][];
		for (int i=0;i < k.row;i++)
		{
			vo[i] = convolve(in.data, k.data[i]);
		}
		return new VMat(vo);
	}
	public static Value[] convolve(Value[][] in, Value[] k)
	{
		Value[] v=null;
		for (int i=0;i < in.length;i++)
		{
			Value[] r=convolve(in[i], k);
			if (v == null)
				v = r;
			else
				v = add(v, r);
		}
		return v;
	}
	public static Value[] convolve(Value[] in, Value[] k)
	{
		Value[] vo=new Value[in.length - k.length + 1];
		int w=0;
		while (w < vo.length)
		{
			Value vt=newValue(0);
			int kp=k.length - 1;
			for (int i=0;i < k.length;i++)
			{
				vt = Value.add(vt, Value.mul(in[i + w], k[kp]));
				kp--;
			}
			vo[w] = vt;
			w++;
		}
		return vo;
	}
	public static Value[] add(Value[] v1, Value[] v2)
	{
		Value[] v3=new Value[Math.max(v1.length, v2.length)];
		for (int i=0;i < v3.length;i++)
		{
			v3[i] = Value.add(v1.length > i ?v1[i]: newValue(0), v2.length > i ?v2[i]: newValue(0));
		}
		return v3;
	}
	public void backward(float d)
	{
		for (int r=0;r < row;r++)
			for (int c=0;c < col;c++)
			{
				data[r][c].backward(d);
			}
	}
	public String getDataAsString()
	{
		String s="[";
		for (int i=0;i < row;i++)
		{
			s += "[";
			for (int j=0;j < col;j++)
			{
				s += data[i][j].data + " , ";
			}
			if (s.trim().endsWith(","))
				s = s.substring(0, s.lastIndexOf(","));
			s += "]\n ";
		}
		s = s.trim() + "]";
		return s;
	}
	public String getGradAsString()
	{
		String s="[";
		for (int i=0;i < row;i++)
		{
			s += "[";
			for (int j=0;j < col;j++)
			{
				s += data[i][j].grad + " , ";
			}
			if (s.trim().endsWith(","))
				s = s.substring(0, s.lastIndexOf(","));
			s += "]\n ";
		}
		s = s.trim() + "]";
		return s;
	}
	@Override
	public String toString()
	{
		String s="mat(" + row + ", " + col + ",= value\n";
		s += getDataAsString();
		return s;
	}

}
