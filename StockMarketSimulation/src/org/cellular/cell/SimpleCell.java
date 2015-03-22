package org.cellular.cell;

import org.cellular.cell.coordinates.Coordinates;
import org.cellular.cell.rules.CellularAutomatonRule;
import org.cellular.world.World;

/**
 * Simple cell implementation which should be good for most use cases. It has
 * value and reevaluated value variables.
 * 
 * @author Ignas
 * 
 * @param <T>
 *            Type of Cell value.
 *
 */
public class SimpleCell<T> implements Cell<T> {

	/** Cell value. */
	private T value;

	/**
	 * Temporal variable for cell new reevaluated value for next world
	 * iteration.
	 */
	private T revaluatedValue;

	/**
	 * Cellular automaton rule which defines what kind of automaton it is. For
	 * most cases this is only thing that needs to be implemented.
	 */
	private CellularAutomatonRule<T> rule;

	/** Cell coordinates in a world. For example x and y in 2d grid world. */
	protected Coordinates cellCoordinates;

	/**
	 * Cell is aware of whole world. {@link CellularAutomatonRule} can retrieve
	 * Cell neighbors with in World and current cell coordinates.
	 */
	protected World<T> world;

	/**
	 * Cell constructor. Cell is provided with its initial value, CA rule, its
	 * coordinates in the world and is made aware of world itself.
	 */
	public SimpleCell(T value, CellularAutomatonRule<T> rule,
			Coordinates cellCoordinates, World<T> world) {
		super();
		this.value = value;
		this.rule = rule;
		this.cellCoordinates = cellCoordinates;
		this.world = world;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getValue() {
		return value;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void revaluateCell() {
		revaluatedValue = rule.calculateNewValue(value, world, cellCoordinates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commitRevaluation() {
		value = revaluatedValue;
		revaluatedValue = null;
	}

}
