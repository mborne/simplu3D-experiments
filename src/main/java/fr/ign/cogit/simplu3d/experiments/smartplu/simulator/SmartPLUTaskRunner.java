package fr.ign.cogit.simplu3d.experiments.smartplu.simulator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import fr.ign.cogit.simplu3d.io.feature.AttribNames;
import fr.ign.cogit.simplu3d.misc.Initialize;
import fr.ign.cogit.simplu3d.util.distribution.ZonePackager;
import fr.ign.simplu3d.iauidf.openmole.EPFIFTask;
import fr.ign.simplu3d.iauidf.tool.ParcelAttributeTransfert;

/**
 * 
 * Simulator for SmartPLU experiment with Numen. It is basically a reuse of
 * IAUODFSimulator with attribute matching as the model is simpler than the one
 * used at the IAUIDF
 * 
 * @author mbrasebin
 *
 */
public class SmartPLUTaskRunner {
	public static String run(File folder, String dirName, File folderOut, File parameterFile, long seed) {

		Initialize.init();

		// We generate cuboid and not trapezoid
		EPFIFTask.USE_DEMO_SAMPLER = false;
		EPFIFTask.INTERSECTION = true;
		EPFIFTask.FLOOR_HEIGHT = 3;
		EPFIFTask.MAX_PARCEL_AREA = 5000; // now obsolete
		EPFIFTask.PARCEL_NAME = "parcelle.shp";
		EPFIFTask.DEBUG_MODE = false;
		// Indicates if simulation is proceeded or not
		EPFIFTask.ATT_SIMUL = "has_rules";

		// Note used attributes
		ParcelAttributeTransfert.att_libelle_zone = "libelle";
		ParcelAttributeTransfert.att_insee = "insee";
		ParcelAttributeTransfert.att_libelle_de_base = "libelle"; // same
																	// attribute
																	// used 2
																	// times
		ParcelAttributeTransfert.att_libelle_de_dul = "libelle"; // same
																	// attribute
																	// used 2
																	// times
		ParcelAttributeTransfert.att_fonctions = "destdomi"; // same attribute
																// used 2 times
		ParcelAttributeTransfert.att_top_zac = ZonePackager.ATTRIBUTE_NAME_BAND; // Random
																					// attribute
		ParcelAttributeTransfert.att_zonage_coherent = ZonePackager.ATTRIBUTE_NAME_BAND; // Random
																							// attribute
		ParcelAttributeTransfert.att_correction_zonage = ZonePackager.ATTRIBUTE_NAME_BAND; // Random
																							// attribute

		// Used attributes
		ParcelAttributeTransfert.att_typ_bande = ZonePackager.ATTRIBUTE_NAME_BAND; // Random
																					// attribute
																					// with
																					// 0
																					// as
																					// value
		ParcelAttributeTransfert.att_bande = ZonePackager.ATTRIBUTE_NAME_BAND; // Random
																				// attribute
																				// with
																				// 0
																				// as
																				// value
		ParcelAttributeTransfert.att_art_5 = "B1_ART_5";
		ParcelAttributeTransfert.att_art_6 = "B1_ART_6";
		ParcelAttributeTransfert.att_art_71 = "B1_ART_71";
		ParcelAttributeTransfert.att_art_72 = "B1_ART_72";
		ParcelAttributeTransfert.att_art_73 = "B1_ART_73";
		ParcelAttributeTransfert.att_art_74 = "B1_ART_74";
		ParcelAttributeTransfert.att_art_8 = "B1_ART_8";
		ParcelAttributeTransfert.att_art_9 = "B1_ART_9";
		ParcelAttributeTransfert.att_art_10 = "B1_ART_10";
		ParcelAttributeTransfert.att_art_10_m = "B1_ART_10";
		ParcelAttributeTransfert.att_art_12 = "B1_ART_14"; // Art 12 is not used
		ParcelAttributeTransfert.att_art_10_top = "B1_ART_10";
		ParcelAttributeTransfert.att_art_13 = "B1_ART_13";
		ParcelAttributeTransfert.att_art_14 = "B1_ART_14";
		AttribNames.setATT_CODE_PARC(ZonePackager.ATTRIBUTE_NAME_ID);

		String imu = dirName;

		String result = "";

		try {
			// Results are stored in an output folder
			result = EPFIFTask.run(folder, dirName, folderOut, parameterFile, seed);
		} catch (Exception e) {
			result = "# " + imu + " #\n";
			result += "# " + e.toString() + "\n";
			result += "# " + imu + " #\n";
			e.printStackTrace();
		}
		return result;
	}

	public static ClassLoader getClassLoader() {
		return SmartPLUTaskRunner.class.getClassLoader();
	}

	public static void main(String[] args) {

		int nbMinimalNumberOfArguments = 4;

		if (args.length < nbMinimalNumberOfArguments) {
			System.out.println(
					"Two arguments expected : rout folder and number of repositories. Extra arguments can be added : the list of parcel ID to simulate");
		}

		// The parent folder Name
		String foldName = args[0]; // "/home/mbrasebin/Documents/Donnees/demo-numen/municipality/61230/out/";

		// The number of repository generated by DataPreparator
		String numrep = args[1]; // "22";

		if (args.length > nbMinimalNumberOfArguments) {
			// If have more attributes, adding to inclusion list
			String[] argsDim = Arrays.copyOfRange(args, nbMinimalNumberOfArguments, args.length);
			EPFIFTask.inclusion_list.addAll(new ArrayList<>(Arrays.asList(argsDim)));
		}

		// The output folder Name
		File folderOut = new File(args[2] + numrep + "/");
		// The parameters file
		File parameterFile = new File(args[3]);
		// "/home/mbrasebin/Documents/Donnees/IAUIDF/Input/Input1/dep_94_connected_openmole/dataBasicSimu/scenario/parameters_iauidf.json"

		File folder = new File(foldName + numrep + "/");
		long seed = 42L;
		String res = "";
		res = run(folder, numrep, folderOut, parameterFile, seed);
		System.out.println("result : " + res);
	}

}
