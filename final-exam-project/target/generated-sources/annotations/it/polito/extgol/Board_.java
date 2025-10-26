package it.polito.extgol;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.MapAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Board.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Board_ {

	
	/**
	 * @see it.polito.extgol.Board#tiles
	 **/
	public static volatile MapAttribute<Board, Coord, Tile> tiles;
	
	/**
	 * @see it.polito.extgol.Board#game
	 **/
	public static volatile SingularAttribute<Board, Game> game;
	
	/**
	 * @see it.polito.extgol.Board#cellSet
	 **/
	public static volatile ListAttribute<Board, Cell> cellSet;
	
	/**
	 * @see it.polito.extgol.Board#width
	 **/
	public static volatile SingularAttribute<Board, Integer> width;
	
	/**
	 * @see it.polito.extgol.Board#id
	 **/
	public static volatile SingularAttribute<Board, Integer> id;
	
	/**
	 * @see it.polito.extgol.Board
	 **/
	public static volatile EntityType<Board> class_;
	
	/**
	 * @see it.polito.extgol.Board#height
	 **/
	public static volatile SingularAttribute<Board, Integer> height;

	public static final String TILES = "tiles";
	public static final String GAME = "game";
	public static final String CELL_SET = "cellSet";
	public static final String WIDTH = "width";
	public static final String ID = "id";
	public static final String HEIGHT = "height";

}

