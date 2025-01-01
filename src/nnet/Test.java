package nnet;

import java.util.*;
import nnet.activation.*;
import nnet.layers.*;
import nnet.loss.*;
import nnet.math.*;
import nnet.optimizers.*;

public class Test
{
	public static void main(String[] args) throws Exception
	{

		new Test().b();

	}
	void b() throws Exception
	{

		Value w1=new Value(2.0);
		Value w2=new Value(3.0);

		Value[] prm={w1,w2};

		Value x=new Value(1);
		Value y=new Value(5);

		for (int i=0;i < 10;i++)
		{
			Value loss= Value.mul(Value.add(Value.pow(Value.sub(w1, x), 2), Value.pow(Value.sub(w2, y), 2)), new Value(0.5));;; // Value.mul(Value.add(Value.pow(Value.sub(w1, x), 2), Value.pow(Value.sub(w2, y), 2)), new Value(0.5));

			System.out.println(loss);
			System.out.println(w1 + ", " + w2);

			for (Value v:prm)
				v.grad = 0;

			loss.backward(1);

			for (Value p:prm)
				p.data -= p.grad * 0.5;

			// treeValue(loss, "");
			// System.out.println(w1.grad + ",, " + w2.grad);
		}
	}
	void treeValue(Value v, String a)
	{
		if (a == null)
			a = "";
		System.out.println(a + v);
		if (v.childs != null)
			for (Value vv:v.childs)
				treeValue(vv, a + "   ");
	}
	void xor()
	{
		// faild.
		System.out.println("xor test");

		Linear l1=new Linear(2, 5);
		Tanh s1=new Tanh();
		Linear l2=new Linear(5, 1);
		Sigmoid s2=new Sigmoid();

		Sequential seq=new Sequential(l1, l2, s2);


		GradientDescent op=new GradientDescent(seq);
		op.setLossFunction(new BCrossEntropy());
		op.setResultCallback(new Result());

		List<VMat[]> inp=new ArrayList<>();
		inp.add(new VMat[]{new VMat(new double[]{0,0}),new VMat(new double[]{0})});
		inp.add(new VMat[]{new VMat(new double[]{0,1}),new VMat(new double[]{1})});
		inp.add(new VMat[]{new VMat(new double[]{1,0}),new VMat(new double[]{1})});
		inp.add(new VMat[]{new VMat(new double[]{1,1}),new VMat(new double[]{0})});

		int count=0;
		for (int c=0;c < 500;c++)
		{
			Collections.shuffle(inp);
			for (int i=0;i < inp.size();i++)
			{
				op.train(inp.get(i)[0], inp.get(i)[1]);
				op.zeroGrad();

				Result rs=op.getResult();
				System.out.println(rs.loss);

				if (count % 200 == 0)
				{
					System.out.println("--------");
					System.out.print(inp.get(i)[0].getDataAsString() + " = ");
					System.out.println(rs.output.getDataAsString());
					System.out.println("--------");
				}
			}
			count++;
		}
		System.out.println("... done ...");

		System.out.println(seq.getParameters());

	}
	void sampleTest() throws Exception
	{

		System.out.println("Hello world!");

		Linear l1=new Linear(10, 100);
		//Tanh l2=new Tanh();
		//Linear l3=new Linear(100, 70);
		Sigmoid l4=new Sigmoid();
//		Linear l5=new Linear(70, 20);
//		Sigmoid l6=new Sigmoid();
		Linear l7=new Linear(100, 7);
		Sigmoid l8=new Sigmoid();
		Sequential s1=new Sequential(l1, l4, l7, l8);
		// s1 = new Sequential(l1, l2, l3, l4, l5, l6, l7, l8);
		GradientDescent gd=new GradientDescent(s1);
		gd.setResultCallback(new Result());

		System.out.println(s1);
		List<VMat[]> data=new ArrayList<VMat[]>();
		for (int i=0;i < 20;i++)
		{
			double[] d=new double[7];
			for (int k=0;k < d.length;k++)
				d[k] = Math.random();
			//.rand(1, 7);
			double[]dd = new double[10];
			for (int k=0;k < d.length;k++)
				dd[k] = Math.random();
			VMat[] c=new VMat[]{new VMat(d),new VMat(dd)};
			data.add(c);
		}
		System.out.println("target " + data.size());
		// Util.pause("See the target random value and enter to continue...");
		int p=0;
		while (true)
		{
			for (int i=0;i < data.size();i++)
			{
				gd.train(data.get(i)[0], data.get(i)[1]);
				System.out.println(p++ + " = " + gd.getResult().loss);
				gd.zeroGrad();
				// Thread.sleep(50);
				// if (Util.count(2000))
				{
					System.out.println(gd.getResult().output);
					System.out.println("??? " + data.get(i)[0]);
					// Util.pause();
				}
			}
			Collections.shuffle(data);
		}
	}
	void GDOptimizerTest()throws Exception
	{
		System.out.println("Gradient deacent.");

		VMat in=VMat.value(1, 5, 0.2f);
		VMat tar=VMat.value(1, 10, 5);
		for (int i=0;i < tar.col;i++)
		{
			tar.put(0, i, i);
		}
		System.out.println("target " + tar);

		Sequential seq=new Sequential();
		seq.addModule(new Linear(5, 2));
		// seq.addModule(new Dropout(0.2f));
		seq.addModule(new Linear(2, 10));
		GradientDescent opt=new GradientDescent(new MSE(), 0.001f);
		opt.setResultCallback(new Result());
		//System.out.println(linear.weight.getDataAsString());
		//System.out.println(linear.biase.getDataAsString());

		while (true)
		{
			opt.train(seq, in, tar);
			System.out.println(opt.getResult().loss);
			opt.zeroGrad();
			// boolean sc=Util.count(500, ">> ");
			//if (sc)
			{
				System.out.println(opt.getResult().output);
				// 	Util.acc();
			}
		}
	}
	void mseTest() throws Exception
	{

		VMat tar=VMat.value(3, 7, 2);
		VMat pred=VMat.ones(3, 7);

		MSE mse=new MSE();

		VMat err=mse.forward(pred, tar);

		System.out.println(tar);
		System.out.println(pred);
		System.out.println(err);
		System.out.println("----");
		err.backward(1);
		System.out.println(pred.getGradAsString());
		System.out.println(tar.getGradAsString());

	}
	void SumVMat() throws Exception
	{

		VMat in=VMat.ones(3, 5);
		VMat sumD=VMat.sum(in, true);
		VMat sum=VMat.sum(in);

		System.out.println(sum);
		System.out.println(sumD);

	}
	void activationFunctions() throws Exception
	{
		System.out.println("sigmoid ====");
		VMat in1=VMat.value(3, 7, 4);
		Sigmoid sig=new Sigmoid();
		VMat sout=sig.forward(in1);
		System.out.println(sout);
		System.out.println(in1.getGradAsString());
		sout.backward(3);
		System.out.println(in1.getGradAsString());

		System.out.println("tanh ====");
		in1 = VMat.value(3, 7, 3);
		Tanh tan=new Tanh();
		sout = tan.forward(in1);
		System.out.println(sout);
		System.out.println(in1.getGradAsString());
		sout.backward(2);
		System.out.println(in1.getGradAsString());

		System.out.println("relu ====");
		in1 = VMat.value(3, 7, 3);
		Relu rl=new Relu();
		sout = rl.forward(in1);
		System.out.println(sout);
		System.out.println(in1.getGradAsString());
		sout.backward(2);
		System.out.println(in1.getGradAsString());


	}
	void flattenTest() throws Exception
	{

		VMat in=VMat.rand(5, 7);

		VMat out=new Flatten().forward(in);

		System.out.println(in);
		System.out.println(out);
		System.out.println("-----");
		System.out.println(in.getGradAsString());
		out.backward(1);
		System.out.println(in.getGradAsString());
	}
	void dropoutTest() throws Exception
	{

		VMat vm=VMat.value(3, 10, 3);
		Dropout dr=new Dropout(0.3f);

		VMat rs=dr.forward(vm);

		System.out.println(vm);
		System.out.println(rs);
		System.out.println("-----");
		System.out.println(vm.getGradAsString());
		rs.backward(1);
		System.out.println(vm.getGradAsString());

	}
	void SequentialTest() throws Exception
	{

		Sequential s=new Sequential();
		Linear l1=new Linear(10, 10);
		s.addModule(l1);
		Conv1d c1=new Conv1d(1, 2, 3);
		s.addModule(c1);
		Conv1d c2=new Conv1d(2, 5, 3);
		s.addModule(c2);

		VMat v=new VMat(new double[][]{{1,2,3,4,5,6,7,8,9,10}});
		VMat out=s.forward(v);
		System.out.println(out);
		System.out.println("------------");
		System.out.println(v.getGradAsString());
		out.backward(1);
		System.out.println("------------");
		System.out.println(v.getGradAsString());

	}
	void convTest() throws Exception
	{
		Conv1d cn=new Conv1d(2, 4, 3);

		VMat v=new VMat(new double[][]{{1,2,3,4},{1,2,3,4}});

		VMat out=cn.forward(v);

		System.out.println(out);
		System.out.println("-----");
		System.out.println(v.getGradAsString());
		out.backward(1);
		System.out.println(v.getGradAsString());

	}
	void rawConvGradTest() throws Exception
	{

		double[][] f={{1,2,3,4,5}};
		double[][] k={{1,2,3},{2,3,4}};

		VMat ff=new VMat(f);
		VMat kk=new VMat(k);

		VMat rs=VMat.convolve(ff, kk);

		System.out.println(rs.toString());
		rs.backward(1);
		System.out.println("gradients");
		System.out.println("c " + rs.getGradAsString());
		System.out.println("f " + kk.getGradAsString());
		System.out.println("i " + ff.getGradAsString());
		System.out.println("-------");

	}
	void maxPool1d() throws Exception
	{

		VMat vm=VMat.rand(3, 20);
		MaxPool1d pool=new MaxPool1d(4);

		System.out.println(vm);
		System.out.println(pool);
		VMat rd=pool.forward(vm);
		System.out.println(rd);
		System.out.println(vm.getGradAsString());
		System.out.println("-------");
		rd.backward(1);
		System.out.println(vm.getGradAsString());

	}
	void linearLayer() throws Exception
	{

		VMat v1=VMat.ones(1, 10);

		Linear ll=new Linear(10, 5, false);

		System.out.println(v1);
		System.out.println(ll);

		System.out.println(ll.weight);
		System.out.println(ll.biase);

		VMat lf=ll.forward(v1);
		System.out.println(lf);

		System.out.println("-----------");
		System.out.println(lf.getGradAsString());
		System.out.println(v1.getGradAsString());

		lf.backward(1);

		System.out.println("-----------");
		System.out.println(lf.getGradAsString());
		System.out.println(v1.getGradAsString());

		System.out.println("-----------");

		System.out.println(ll.getParameters());

	}
	void instanceCheck() throws Exception
	{
		VMat v0=VMat.value(6, 3, 9);
		VMat v1=VMat.zeros(2, 5);
		VMat v2=VMat.ones(5, 3);
		VMat v3=VMat.eye(3, 2);
		VMat v4=VMat.rand(4, 4);
		VMat v5=VMat.eye(7);

		System.out.println("value " + v0);
		System.out.println("zero " + v1);
		System.out.println("one " + v2);
		System.out.println("eye 1 " + v3);
		System.out.println("rand " + v4);
		System.out.println("eye 2 " + v5);

	}
}
