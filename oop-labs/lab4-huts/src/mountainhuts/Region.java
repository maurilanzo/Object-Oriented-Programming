package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {

	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */

	private String name;

	public Region(String name) {
		this.name=name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	List<Altitude> altitudes = new ArrayList<>();
	public void setAltitudeRanges(String... ranges) {
		String [] altitudeRanges = ranges;
		for (String range : altitudeRanges) {
			String[] parts = range.split("-");
			Integer min = Integer.parseInt(parts[0]);
			Integer max = Integer.parseInt(parts[1]);
			Altitude a = new Altitude(min,max);
			altitudes.add(a);
		}

	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		String range = altitudes.stream().filter(p->p.isIn(altitude)).map(Altitude::toString).findFirst().orElse("0-INF");
		return range;
	}
	

	/**
	 * Return all the municipalities available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return municipalities.values();
	}
	
	/**
	 * Return all the mountain huts available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return huts.values();
	}
	
	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	private HashMap<String, Municipality> municipalities = new HashMap<>();
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		Municipality m = municipalities.get(name);
		if (m==null){
			m = new Municipality(name, province, altitude);
			municipalities.put(name,m);
			return m;
		}
		else {
			return m;
		}
		
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	private HashMap<String, MountainHut> huts = new HashMap<>();
	public MountainHut createOrGetMountainHut(String name, String category, 
											  Integer bedsNumber, Municipality municipality) {
		
		MountainHut h = huts.get(name);
		if (h==null){
			h = new MountainHut(name,category,bedsNumber,municipality,null);
			huts.put(name,h);
			return h;
		}
		else {
			return h;
		}
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, 
											  Integer bedsNumber, Municipality municipality) {
		MountainHut h = huts.get(name);
		if (h==null){
			h = new MountainHut(name,category,bedsNumber,municipality,altitude);
			huts.put(name,h);
			return h;
		}
		else {
			return h;
		}
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		List<String> x = new ArrayList<>();
		List<String[]> lines = new ArrayList<>();

		Objects.requireNonNull(name);
		Objects.requireNonNull(file);

		Region r = new Region(name);
		x = readData(file);

		for(String s:x) lines.add(s.split(";"));
		lines.remove(0);

		for (String[] s: lines){
			Municipality m = r.createOrGetMunicipality(s[1],s[0],Integer.valueOf(s[2]));
			
			
			MountainHut h;
			if (s[4]=="") r.createOrGetMountainHut(s[3], s[5], Integer.valueOf(s[6]),m);
			else r.createOrGetMountainHut(s[3], Integer.valueOf(s[4]), s[5], Integer.valueOf(s[6]), m);			
		}
	
		return r;
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().toList();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {

		return municipalities.values().stream().collect(Collectors.groupingBy(Municipality::getProvince,Collectors.counting()));
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		return huts.values()
				.stream()
				.collect(Collectors.groupingBy(hut-> hut.getMunicipality().getProvince(),Collectors.groupingBy(hut-> hut.getMunicipality().getName(),Collectors.counting())));
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		return huts.values().stream().collect(Collectors.groupingBy(h-> h.getAltitude().isPresent()? getAltitudeRange(h.getAltitude().get()) : getAltitudeRange(h.getMunicipality().getAltitude()),Collectors.counting()));
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		return huts.values()
				.stream()
				.collect(Collectors.groupingBy(hut-> hut.getMunicipality().getProvince(),Collectors.summingInt(MountainHut::getBedsNumber)));
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		return huts.values().stream().collect(Collectors.groupingBy(h-> h.getAltitude().isPresent()? getAltitudeRange(h.getAltitude().get()) : getAltitudeRange(h.getMunicipality().getAltitude()),Collectors.mapping(MountainHut::getBedsNumber,Collectors.maxBy((i1,i2)-> Integer.compare(i1,i2)))));
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		Map<String, Long> huts_muni = huts.values().stream().collect(
			Collectors.groupingBy(
				h->h.getMunicipality().getName(),
				Collectors.counting()
			)
		);
		return huts_muni.entrySet().stream().collect(
			Collectors.groupingBy(
				Map.Entry<String,Long>::getValue,
				Collectors.collectingAndThen(
					Collectors.mapping(
						Map.Entry<String,Long>::getKey,
						Collectors.toList()
					),
					l->l.stream().sorted().collect(Collectors.toList())
				)
			)
		);
	}

}
