package it.polito.extgol;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Tile.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Tile_ {

	
	/**
	 * @see it.polito.extgol.Tile#lifePointModifier
	 **/
	public static volatile SingularAttribute<Tile, Integer> lifePointModifier;
	
	/**
	 * @see it.polito.extgol.Tile#game
	 **/
	public static volatile SingularAttribute<Tile, Game> game;
	
	/**
	 * @see it.polito.extgol.Tile#id
	 **/
	public static volatile SingularAttribute<Tile, Long> id;
	
	/**
	 * @see it.polito.extgol.Tile#tileCoord
	 **/
	public static volatile SingularAttribute<Tile, Coord> tileCoord;
	
	/**
	 * @see it.polito.extgol.Tile#cell
	 **/
	public static volatile SingularAttribute<Tile, Cell> cell;
	
	/**
	 * @see it.polito.extgol.Tile
	 **/
	public static volatile EntityType<Tile> class_;
	
	/**
	 * @see it.polito.extgol.Tile#board
	 **/
	public static volatile SingularAttribute<Tile, Board> board;

	public static final String LIFE_POINT_MODIFIER = "lifePointModifier";
	public static final String GAME = "game";
	public static final String ID = "id";
	public static final String TILE_COORD = "tileCoord";
	public static final String CELL = "cell";
	public static final String BOARD = "board";

}

