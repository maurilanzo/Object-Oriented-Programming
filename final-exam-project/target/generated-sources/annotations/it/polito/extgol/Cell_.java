package it.polito.extgol;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Cell.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Cell_ {

	
	/**
	 * @see it.polito.extgol.Cell#isAlive
	 **/
	public static volatile SingularAttribute<Cell, Boolean> isAlive;
	
	/**
	 * @see it.polito.extgol.Cell#game
	 **/
	public static volatile SingularAttribute<Cell, Game> game;
	
	/**
	 * @see it.polito.extgol.Cell#cellType
	 **/
	public static volatile SingularAttribute<Cell, CellType> cellType;
	
	/**
	 * @see it.polito.extgol.Cell#mood
	 **/
	public static volatile SingularAttribute<Cell, CellMood> mood;
	
	/**
	 * @see it.polito.extgol.Cell#empoweredBite
	 **/
	public static volatile SingularAttribute<Cell, Boolean> empoweredBite;
	
	/**
	 * @see it.polito.extgol.Cell#tile
	 **/
	public static volatile SingularAttribute<Cell, Tile> tile;
	
	/**
	 * @see it.polito.extgol.Cell#lifepoints
	 **/
	public static volatile SingularAttribute<Cell, Integer> lifepoints;
	
	/**
	 * @see it.polito.extgol.Cell#id
	 **/
	public static volatile SingularAttribute<Cell, Long> id;
	
	/**
	 * @see it.polito.extgol.Cell
	 **/
	public static volatile EntityType<Cell> class_;
	
	/**
	 * @see it.polito.extgol.Cell#cellCoord
	 **/
	public static volatile SingularAttribute<Cell, Coord> cellCoord;
	
	/**
	 * @see it.polito.extgol.Cell#board
	 **/
	public static volatile SingularAttribute<Cell, Board> board;

	public static final String IS_ALIVE = "isAlive";
	public static final String GAME = "game";
	public static final String CELL_TYPE = "cellType";
	public static final String MOOD = "mood";
	public static final String EMPOWERED_BITE = "empoweredBite";
	public static final String TILE = "tile";
	public static final String LIFEPOINTS = "lifepoints";
	public static final String ID = "id";
	public static final String CELL_COORD = "cellCoord";
	public static final String BOARD = "board";

}

