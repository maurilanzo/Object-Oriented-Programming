package mountainhuts;

import java.util.Optional;

/**
 * Represents a mountain hut
 * 
 * It includes a name, optional altitude, category,
 * number of beds and location municipality.
 *  
 *
 */

public class MountainHut {
	private String name;
	private Integer altitude;
	private String category;
	private Integer beds;
	private Municipality muni;

	/**
	 * Retrieves the name of the hut
	 * @return name of the hut
	 */
	public MountainHut(String name, String category, Integer beds, Municipality muni, Integer altitude){
		this.name=name;
		this.category=category;
		this.beds=beds;
		this.muni=muni;
		this.altitude=altitude;
	}

	public String getName() {
		return name;
	}

	/**
	 * Retrieves altituted if available
	 * 
	 * @return optional hut altitude
	 */
	public Optional<Integer> getAltitude() {
		return Optional.ofNullable(altitude);
	}

	/**
	 * Retrieves the category of the hut
	 * @return hut category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Retrieves the number of beds available in the hut
	 * @return number of beds
	 */
	public Integer getBedsNumber() {
		return beds;
	}

	/**
	 * Retrieves the municipality of the hut
	 * @return hut municipality
	 */
	public Municipality getMunicipality() {
		return muni;
	}
}
