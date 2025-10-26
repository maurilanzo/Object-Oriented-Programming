package it.polito.extgol;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Generation.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Generation_ {

	
	/**
	 * @see it.polito.extgol.Generation#energyStates
	 **/
	public static volatile MapAttribute<Generation, Cell, Integer> energyStates;
	
	/**
	 * @see it.polito.extgol.Generation#game
	 **/
	public static volatile SingularAttribute<Generation, Game> game;
	
	/**
	 * @see it.polito.extgol.Generation#step
	 **/
	public static volatile SingularAttribute<Generation, Integer> step;
	
	/**
	 * @see it.polito.extgol.Generation#id
	 **/
	public static volatile SingularAttribute<Generation, Long> id;
	
	/**
	 * @see it.polito.extgol.Generation#cellsMood
	 **/
	public static volatile MapAttribute<Generation, Cell, CellMood> cellsMood;
	
	/**
	 * @see it.polito.extgol.Generation#event
	 **/
	public static volatile SingularAttribute<Generation, EventType> event;
	
	/**
	 * @see it.polito.extgol.Generation
	 **/
	public static volatile EntityType<Generation> class_;
	
	/**
	 * @see it.polito.extgol.Generation#board
	 **/
	public static volatile SingularAttribute<Generation, Board> board;
	
	/**
	 * @see it.polito.extgol.Generation#cellAlivenessStates
	 **/
	public static volatile MapAttribute<Generation, Cell, Boolean> cellAlivenessStates;

	public static final String ENERGY_STATES = "energyStates";
	public static final String GAME = "game";
	public static final String STEP = "step";
	public static final String ID = "id";
	public static final String CELLS_MOOD = "cellsMood";
	public static final String EVENT = "event";
	public static final String BOARD = "board";
	public static final String CELL_ALIVENESS_STATES = "cellAlivenessStates";

}

