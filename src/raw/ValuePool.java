package raw;

import java.util.*;

public class ValuePool
{
	public static List<VMat> mats=new ArrayList<>();
	public static List<Value> vals=new ArrayList<>();
	public static int r,c;

	public static Value newValue(float d, Value...childs)
	{
		if (vals.size() != 0)
			return vals.remove(0).set(d, childs);
		if (mats.size() != 0)
		{
			VMat vm=mats.get(0);
			Value vl=vm.get(r, c);
			c++;
			if (c >= vm.col)
			{
				c = 0;
				r++;
			}
			if (r >= vm.row)
			{
				r = 0;
				c = 0;
				mats.remove(0);
			}
			return vl;
		}
		return new Value(d, childs);
	}
	public static void recycle(Value v)
	{
		vals.add(v);
	}
	public static void recycle(VMat vm)
	{
		mats.add(vm);
	}
}
