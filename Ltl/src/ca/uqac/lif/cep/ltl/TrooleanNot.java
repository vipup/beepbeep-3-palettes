/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2017 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.ltl;

import java.util.ArrayDeque;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.functions.ApplyFunction;
import ca.uqac.lif.cep.Processor;

/**
 * Troolean implementation of logical negation.
 * @author Sylvain Hallé
 */
public class TrooleanNot extends ApplyFunction 
{
	public TrooleanNot()
	{
		super(Troolean.NOT_FUNCTION);
	}
	
	public static void build(ArrayDeque<Object> stack) 
	{
		stack.pop(); // (
		Processor p = (Processor) stack.pop();
		stack.pop(); // )
		stack.pop(); // NOT
		TrooleanNot op = new TrooleanNot();
		Connector.connect(p, op);
		stack.push(op);
	}
	
	@Override
	public TrooleanNot duplicate()
	{
		return new TrooleanNot();
	}
}
