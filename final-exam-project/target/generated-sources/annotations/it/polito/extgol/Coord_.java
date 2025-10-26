package it.polito.extgol;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EmbeddableType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Coord.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Coord_ {

	
	/**
	 * @see it.polito.extgol.Coord#x
	 **/
	public static volatile SingularAttribute<Coord, Integer> x;
	
	/**
	 * @see it.polito.extgol.Coord#y
	 **/
	public static volatile SingularAttribute<Coord, Integer> y;
	
	/**
	 * @see it.polito.extgol.Coord
	 **/
	public static volatile EmbeddableType<Coord> class_;

	public static final String X = "x";
	public static final String Y = "y";

}

