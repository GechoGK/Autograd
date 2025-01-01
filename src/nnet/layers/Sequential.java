package nnet.layers;

import java.util.*;
import nnet.*;
import nnet.math.*;

public class Sequential extends Module
{
	public List<Module> modules=new ArrayList<>();

	public Sequential(Module...ms)
	{
		for (Module m:ms)
			modules.add(m);
	}

	public Module addModule(Module module)
	{
		modules.add(module);
		return module;
	}
	public Module rmoveModule(Module m)
	{
		modules.remove(m);
		return m;
	}
	@Override
	public VMat forward(VMat in)
	{
		for (Module m:modules)
			in = m.forward(in);
		return in;
	}

	@Override
	public List<Mat> getParameters()
	{
		params.clear();
		for (Module m:modules)
			params.addAll(m.getParameters());
		return params;
	}
	@Override
	public String toString()
	{
		String s = "sequential module(" + modules.size() + " child modules)\n";
		for (int i=0;i < modules.size();i++)
			s += "  --> " + modules.get(i) + (i == modules.size() - 1 ?"": "\n");
		return s;
	}
}
