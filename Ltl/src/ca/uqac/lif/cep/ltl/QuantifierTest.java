package ca.uqac.lif.cep.ltl;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.GroupProcessor;
import ca.uqac.lif.cep.ProcessorException;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.concurrency.NonBlockingPusher;
import ca.uqac.lif.cep.concurrency.ThreadManager;
import ca.uqac.lif.cep.functions.ContextVariable;
import ca.uqac.lif.cep.util.Equals;
import ca.uqac.lif.cep.util.Numbers;
import ca.uqac.lif.cep.functions.Function;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.functions.FunctionTree;
import ca.uqac.lif.cep.functions.StreamVariable;
import ca.uqac.lif.cep.functions.UnaryFunction;
import ca.uqac.lif.cep.tmf.Fork;
import ca.uqac.lif.cep.tmf.Passthrough;
import ca.uqac.lif.cep.tmf.QueueSink;
import ca.uqac.lif.cep.tmf.QueueSource;

/**
 * Unit tests for quantifiers
 * @author Sylvain Hallé
 */
public class QuantifierTest 
{
	@Test
	public void testEveryPush1() 
	{
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, StreamVariable.X);
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gt);
		QueueSink sink = new QueueSink(1);
		Connector.connect(fa, sink);
		Pushable in = fa.getPushableInput(0);
		Queue<Object> queue = sink.getQueue(0);
		in.push(0);
		assertFalse(queue.isEmpty());
		Object output = queue.remove();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.FALSE, (Troolean.Value) output);
	}
	
	@Test
	public void testEveryPull1() 
	{
		QueueSource source = new QueueSource(1);
		source.addEvent(0);
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, StreamVariable.X);
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gt);
		Connector.connect(source, fa);
		Pullable out = fa.getPullableOutput(0);
		Object output = out.pull();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.FALSE, (Troolean.Value) output);
	}
	
	@Test
	public void testEvery2() 
	{
		FunctionTree tree = new FunctionTree(Numbers.isLessThan); 
		tree.setChild(0, StreamVariable.X);
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gt);
		QueueSink sink = new QueueSink(1);
		Connector.connect(fa, sink);
		Pushable in = fa.getPushableInput(0);
		Queue<Object> queue = sink.getQueue(0);
		in.push(0);
		assertFalse(queue.isEmpty());
		Object output = queue.remove();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.TRUE, (Troolean.Value) output);
		in.push(10);
		assertFalse(queue.isEmpty());
		output = queue.remove();
		assertEquals(Troolean.Value.TRUE, (Troolean.Value) output);
	}
	
	@Test
	public void testEvery3() 
	{
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, new ContextVariable("x"));
		tree.setChild(1, new ContextVariable("y"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gt);
		Some fa2 = new Some("y", new DummyCollectionFunction(1, 2, 3), fa);
		QueueSink sink = new QueueSink(1);
		Connector.connect(fa2, sink);
		Pushable in = fa2.getPushableInput(0);
		Queue<Object> queue = sink.getQueue(0);
		in.push(10);
		assertFalse(queue.isEmpty());
		Object output = queue.remove();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.FALSE, (Troolean.Value) output);
	}
	
	@Test
	public void testEveryPull3() 
	{
		QueueSource source = new QueueSource(1);
		source.addEvent(0);
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, new ContextVariable("x"));
		tree.setChild(1, new ContextVariable("y"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gt);
		Some fa2 = new Some("y", new DummyCollectionFunction(1, 2, 3), fa);
		Connector.connect(source, fa2);
		Pullable out = fa2.getPullableOutput(0);
		Object output = out.pullSoft();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.FALSE, (Troolean.Value) output);
	}
	
	@Test
	public void testEvery4() 
	{
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, new ContextVariable("y"));
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gt);
		Some fa2 = new Some("y", new DummyCollectionFunction(2, 3, 4), fa);
		QueueSink sink = new QueueSink(1);
		Connector.connect(fa2, sink);
		Pushable in = fa2.getPushableInput(0);
		Queue<Object> queue = sink.getQueue(0);
		in.push(10);
		assertFalse(queue.isEmpty());
		Object output = queue.remove();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.TRUE, (Troolean.Value) output);
	}
	
	@Test
	public void testEveryPull5() 
	{
		QueueSource source = new QueueSource(1);
		source.addEvent(0);
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, new ContextVariable("y"));
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Every fa = new Every("x", new DummyCollectionFunction(2), gt);
		Every fa2 = new Every("y", new DummyCollectionFunction(1), fa);
		Connector.connect(source, fa2);
		Pullable out = fa2.getPullableOutput(0);
		Object output = out.pull();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.FALSE, (Troolean.Value) output);
	}
	
	@Test
	public void testSome1() 
	{
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, StreamVariable.X);
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Some fa = new Some("x", new DummyCollectionFunction(1, 2, 3), gt);
		QueueSink sink = new QueueSink(1);
		Connector.connect(fa, sink);
		Pushable in = fa.getPushableInput(0);
		Queue<Object> queue = sink.getQueue(0);
		in.push(0);
		assertFalse(queue.isEmpty());
		Object output = queue.remove();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.FALSE, (Troolean.Value) output);
	}

	@Test
	public void testSome2() 
	{
		FunctionTree tree = new FunctionTree(Numbers.isGreaterThan); 
		tree.setChild(0, StreamVariable.X);
		tree.setChild(1, new ContextVariable("x"));
		ApplyFunction gt = new ApplyFunction(tree);
		Some fa = new Some("x", new DummyCollectionFunction(1, 2, 3), gt);
		QueueSink sink = new QueueSink(1);
		Connector.connect(fa, sink);
		Pushable in = fa.getPushableInput(0);
		Queue<Object> queue = sink.getQueue(0);
		in.push(2);
		assertFalse(queue.isEmpty());
		Object output = queue.remove();
		assertNotNull(output);
		assertTrue(output instanceof Troolean.Value);
		assertEquals(Troolean.Value.TRUE, (Troolean.Value) output);
	}
	
	@Test
	public void testGroupClone1() 
	{
		Pullable p;
		Object o;
		TrooleanImplies imp = new TrooleanImplies();
		Fork fork = new Fork(2);
		ApplyFunction left = new ApplyFunction(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)));
		ApplyFunction right = new ApplyFunction(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)));
		Connector.connect(fork, 0, left, 0);
		Connector.connect(fork, 1, right, 0);
		Connector.connect(left, 0, imp, 0);
		Connector.connect(right, 0, imp, 1);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessors(imp, fork, left, right);
		gp.associateInput(0, fork, 0);
		gp.associateOutput(0, imp, 0);
		// Check that first group works
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, gp);
		p = gp.getPullableOutput(0);
		o = p.pull();
		assertEquals(o, Troolean.Value.TRUE);
		// Now clone and re-check
		GroupProcessor gp_clone = gp.duplicate();
		QueueSource source2 = new QueueSource(1);
		source2.addEvent(0);
		Connector.connect(source2, gp_clone);
		p = gp_clone.getPullableOutput(0);
		o = p.pull();
		assertEquals(o, Troolean.Value.TRUE);
	}
	
	@Test
	public void testGroupClone2() 
	{
		Pullable p;
		Object o;
		TrooleanImplies imp = new TrooleanImplies();
		Fork fork = new Fork(2);
		ApplyFunction left = new ApplyFunction(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)));
		ApplyFunction right = new ApplyFunction(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, new ContextVariable("x"))));
		Connector.connect(fork, 0, left, 0);
		Connector.connect(fork, 1, right, 0);
		Connector.connect(left, 0, imp, 0);
		Connector.connect(right, 0, imp, 1);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessors(imp, fork, left, right);
		gp.associateInput(0, fork, 0);
		gp.associateOutput(0, imp, 0);
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), gp);
		// Check that first group works
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, fa);
		p = fa.getPullableOutput(0);
		o = p.pull();
		assertEquals(o, Troolean.Value.FALSE);
		// Now clone and re-check
		Every gp_clone = fa.duplicate();
		QueueSource source2 = new QueueSource(1);
		source2.addEvent(0);
		Connector.connect(source2, gp_clone);
		p = gp_clone.getPullableOutput(0);
		o = p.pull();
		assertEquals(o, Troolean.Value.FALSE);
	}
	
	@Test
	public void testGroupClone3() 
	{
		Pullable p;
		Object o;
		Passthrough pt = new Passthrough(1);
		ApplyFunction left = new ApplyFunction(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)));
		Every fa = new Every("x", new DummyCollectionFunction(1, 2, 3), left);
		Connector.connect(pt, fa);
		GroupProcessor gp = new GroupProcessor(1, 1);
		gp.addProcessors(pt, fa);
		gp.associateInput(0, pt, 0);
		gp.associateOutput(0, fa, 0);
		// Check that first group works
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, fa);
		p = fa.getPullableOutput(0);
		o = p.pull();
		assertEquals(o, Troolean.Value.TRUE);
		// Now clone and re-check
		Every gp_clone = fa.duplicate();
		QueueSource source2 = new QueueSource(1);
		source2.addEvent(0);
		Connector.connect(source2, gp_clone);
		p = gp_clone.getPullableOutput(0);
		o = p.pull();
		assertEquals(o, Troolean.Value.TRUE);
		o = p.pull();
		assertEquals(o, Troolean.Value.TRUE);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testForAll1() 
	{
		ThreadManager tm = new ThreadManager(-1); // Unlimited threads
		SlowFunctionProcessor left = new SlowFunctionProcessor(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)), 0);
		BooleanQuantifier fa = new BooleanQuantifier(new ForAllSpawn("x", left, new DummyCollectionFunction(1, 2, 3)));
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, fa);
		Pullable p = fa.getPullableOutput(0);
		Object o = p.pull();
		assertNotNull(o);
		assertNotNull(o);
		assertEquals(o, Troolean.Value.TRUE);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testForAll2() 
	{
		ThreadManager tm = new ThreadManager(-1); // Unlimited threads
		SlowFunctionProcessor left = new SlowFunctionProcessor(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)), 0);
		ForAllSpawn fas = new ForAllSpawn("x", left, new DummyCollectionFunction(1, 2, 3));
		NonBlockingPusher nbp = new NonBlockingPusher(fas);
		BooleanQuantifier fa = new BooleanQuantifier(nbp);
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, fa);
		Pullable p = fa.getPullableOutput(0);
		Object o = p.pull();
		assertNotNull(o);
		assertNotNull(o);
		assertEquals(o, Troolean.Value.TRUE);
	}
	
	@Test
	public void testForAll3() throws ProcessorException
	{
		ThreadManager tm = new ThreadManager(-1); // Unlimited threads
		SlowFunctionProcessor left = new SlowFunctionProcessor(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, new ContextVariable("x"), new ContextVariable("z"))), 0);
		ForAllSpawn fas = new ForAllSpawn("x", left, new DummyCollectionFunction(1, 2, 3));
		NonBlockingPusher nbp = new NonBlockingPusher(fas, tm);
		nbp.start();
		BooleanQuantifier fa = new BooleanQuantifier(nbp);
		ForAllSpawn fa2 = new ForAllSpawn("z", fa, new DummyCollectionFunction(1, 2, 3));
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, fa2);
		Pullable p = fa2.getPullableOutput(0);
		Object o = p.pull();
		assertNotNull(o);
		assertNotNull(o);
		assertEquals(Troolean.Value.FALSE, o);
		nbp.stop();
	}
	
	@Test
	public void testNonBlockingInQuantifier() 
	{
		ThreadManager tm = new ThreadManager(-1); // Unlimited threads
		SlowFunctionProcessor left = new SlowFunctionProcessor(new FunctionTree(TrooleanCast.instance, new FunctionTree(Equals.instance, StreamVariable.X, StreamVariable.X)), 1000);
		ForAllSpawn fas = new ForAllSpawn("x", left, new DummyCollectionFunction(1, 2, 3));
		NonBlockingPusher pp = new NonBlockingPusher(fas, tm);
		BooleanQuantifier fa = new BooleanQuantifier(pp);
		QueueSource source1 = new QueueSource(1);
		source1.addEvent(0);
		Connector.connect(source1, fa);
		Pullable p = fa.getPullableOutput(0);
		long time_before = System.currentTimeMillis();
		Object o = p.pull();
		long time_after = System.currentTimeMillis();
		System.out.println("TIME: " + (time_after - time_before));
		assertNotNull(o);
		assertEquals(o, Troolean.Value.TRUE);
	}

	@SuppressWarnings("rawtypes")
	public static class DummyCollectionFunction extends UnaryFunction<Object,Set>
	{
		Set<Integer> m_values = new HashSet<Integer>();
		
		public DummyCollectionFunction(Integer ... values)
		{
			super(Object.class, Set.class);
			for (Integer v : values)
			{
				m_values.add(v);
			}
		}

		@Override
		public Set<Integer> getValue(Object x) 
		{
			return m_values;
		}
	}
	
	public static class DummyDomainFunction extends UnaryFunction<Integer,Integer>
	{
		public DummyDomainFunction()
		{
			super(Integer.class, Integer.class);
		}

		@Override
		public Integer getValue(Integer x) 
		{
			return x;
		}
	}
	
	public static class DummyBooleanFunction extends UnaryFunction<Integer,Boolean>
	{
		public DummyBooleanFunction()
		{
			super(Integer.class, Boolean.class);
		}

		@Override
		public Boolean getValue(Integer x) 
		{
			return x.intValue() % 2 == 0;
		}
	}
	
	public static class IsEvenProcessor extends ApplyFunction
	{
		public IsEvenProcessor()
		{
			super(new DummyBooleanFunction());
		}
	}
	
	/**
	 * Like a function processor, but waits before returning its answer
	 */
	public static class SlowFunctionProcessor extends ApplyFunction
	{
		protected long m_waitInterval;

		public SlowFunctionProcessor(Function comp, long wait_interval)
		{
			super(comp);
			m_waitInterval = wait_interval;
		}
		
		@Override
		public boolean compute(Object[] inputs, Object[] outputs) throws ProcessorException
		{
			ThreadManager.sleep(m_waitInterval);
			return super.compute(inputs, outputs);
		}

		@Override
		public SlowFunctionProcessor duplicate()
		{
			SlowFunctionProcessor sfp = new SlowFunctionProcessor(getFunction().duplicate(), m_waitInterval);
			return sfp;
		}
	}

}
