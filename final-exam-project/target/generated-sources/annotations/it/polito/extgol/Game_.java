package it.polito.extgol;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Game.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Game_ {

	
	/**
	 * @see it.polito.extgol.Game#eventMap
	 **/
	public static volatile MapAttribute<Game, Integer, EventType> eventMap;
	
	/**
	 * @see it.polito.extgol.Game#generations
	 **/
	public static volatile ListAttribute<Game, Generation> generations;
	
	/**
	 * @see it.polito.extgol.Game#name
	 **/
	public static volatile SingularAttribute<Game, String> name;
	
	/**
	 * @see it.polito.extgol.Game#id
	 **/
	public static volatile SingularAttribute<Game, Long> id;
	
	/**
	 * @see it.polito.extgol.Game
	 **/
	public static volatile EntityType<Game> class_;
	
	/**
	 * @see it.polito.extgol.Game#board
	 **/
	public static volatile SingularAttribute<Game, Board> board;

	public static final String EVENT_MAP = "eventMap";
	public static final String GENERATIONS = "generations";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String BOARD = "board";

}

