package raw;

import static raw.ValuePool.*;

public class Value
{
	public float data=0,grad=0;
	public Value[] childs;

	public void backward()
	{
		for (Value v:childs)
			v.backward();
	}
	public void backward(float grad)
	{
		this.grad = grad;
		backward();
	}
	public Value get(int p)
	{
		if (p >= childs.length)
			throw new RuntimeException("unable to access child at " + p);
		return childs[p];
	}
	public Value(float v, Value...chlds)
	{
		this.data = v;
		this.childs = chlds;
	}
	public Value set(float d, Value...chld)
	{
		this.data = d;
		this.childs = chld;
		return this;
	}
	@Override
	public String toString()
	{
		return "value {data =" + data + " , grad =" + grad + "}";
	}
	public static Value add(Value op1, Value op2)
	{
		Value v=new Value(op1.data + op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].grad += this.grad;
				this.childs[1].grad += this.grad;
				super.backward();
			}
		};
		return v;
	}
	public static Value mul(Value op1, Value op2)
	{
		Value v=new Value(op1.data * op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].grad += this.grad * this.childs[1].data;
				this.childs[1].grad += this.grad * this.childs[0].data;
				super.backward();
			}
		};
		return v;
	}
	public static Value sub(Value op1, Value op2)
	{
		Value v=new Value(op1.data - op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].grad += this.grad;
				this.childs[1].grad += -this.grad;
				super.backward();
			}
		};
		return v;
	}
	public static Value div(Value op1, Value op2)
	{
		Value v=new Value(op1.data / op2.data, op1, op2){
			public void backward()
			{
				this.childs[0].grad += this.grad * 1 / this.childs[1].data;
				this.childs[1].grad += -this.grad * this.childs[0].data / (childs[1].data * childs[1].data);
				super.backward();
				// -(grad * getArg(0) / (getArg(1) * getArg(1));
			}
		};
		return v;
	}
	public static Value pow(Value op1, Value op2)
	{
		Value v=new Value((float)Math.pow(op1.data , op2.data), op1, op2){
			// inputs.get(0).backward(grad * getArg(1) * Math.pow(getArg(0), getArg(1) - 1));
			// inputs.get(1).backward(grad * (Math.pow(getArg(0), getArg(1)) * Math.log(getArg(0))));
			public void backward()
			{
				this.childs[0].grad += this.grad * this.childs[1].data * Math.pow(this.childs[0].data, childs[1].data - 1);
				this.childs[1].grad += this.grad * (Math.pow(this.childs[0].data, this.childs[1].data) * Math.log(childs[0].data));
				super.backward();
			}
		};
		return v;
	}
	public class Func
	{
		public void backward()
		{}
	}
}
