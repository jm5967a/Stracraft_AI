package bot;

/**
 * Example of a Java AI Client that does nothing.
 */
//import com.sun.javafx.sg.prism.NGWebView;
import com.sun.glass.ui.SystemClipboard;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.geometry.Pos;
import javafx.scene.layout.TilePane;

import jnibwapi.*;
import jnibwapi.BaseLocation;
import jnibwapi.Map;
import jnibwapi.types.UnitType;

import java.util.*;


public class ExampleAIClient implements BWAPIEventListener {
	private JNIBWAPI bwapi;
	private final HashSet<Unit> claimedMinerals = new HashSet<>();
	int o = 0;
	Position supply_pylon = new Position(0, 0);
	int supply_count = 0;
	LinkedList pylons = new LinkedList();
	int gateway = 0;
	int check = 0;
	int cloaked = 0;
	boolean air = false;
	boolean forge = false;
	Position base;
	boolean assimilated = false;
	LinkedList Attack = new LinkedList();
	int reset = 0;
	int race = 0;
	int gasProbes = 0;
	LinkedList Zealots = new LinkedList();
	LinkedList Dragoon = new LinkedList();
	boolean completion = false;
	boolean cybercored = false;
	boolean templararchives = false;
	boolean stargate = false;
	boolean fleetbacon = false;
	boolean photon =false;
	int counter = 0;
	int building = 0;
	int dragoon = 0;
	Unit worker;
	int location;
	int override = 0;
	Position origin;
	Position distance;
	Position runover;
	Position runoverorigin;
	boolean gas = false;
	int interrupt = 1;
	Position buildpylon;

	public static void main(String[] args) {

		new ExampleAIClient();
	}

	public ExampleAIClient() {
		bwapi = new JNIBWAPI(this, false);
		bwapi.start();
	}

	@Override
	public void connected() {
	}

	@Override
	public void matchStart() {
		System.out.println("Game Started");

		bwapi.enableUserInput();
		bwapi.enablePerfectInformation();
		bwapi.setGameSpeed(20);
		claimedMinerals.clear();


	}

	@Override
	public void matchFrame() {

		//if we're running out of supply and have enough minerals ...
		Player x = bwapi.getSelf();

		probe();
		if (!cybercored) {
			earlygame(x);
		}
		if (cybercored) {
			midgame(x);
		}

		if (x.getSupplyUsed() + 3 >= x.getSupplyTotal()) {
			int count = 0;
			for (Unit units : bwapi.getUnits(x)) {
				if (units.getType() == UnitType.UnitTypes.Protoss_Pylon && units.isCompleted() == true) {
					count += 1;
				}
			}
			if (count == supply_count + 1) {
				o = 0;
				supply_count += 1;

			}
		}
		if (x.getSupplyUsed() + 3 >= x.getSupplyTotal() && o == 0 && x.getMinerals() > 150) {

			supply();
			o = 1;
			;
		}

		for (Unit assim : bwapi.getUnits(x)) {
			if (gas) {
				if (assim.getType().isRefinery() && gasProbes < 3) {
					if (assim.isCompleted()) {
						;
						for (Unit myUnit : bwapi.getUnits(x)) {
							if ((myUnit.getType().isWorker() || myUnit.isGatheringMinerals()) && gasProbes < 3) {
								myUnit.gather(assim, false);
								gasProbes++;
							}

						}

					}
				}
			}
		}
		for (Unit myUnit : bwapi.getUnits(x)) {
			if (myUnit.getType() == UnitType.UnitTypes.Protoss_Probe) {
				if (myUnit.isIdle() && !myUnit.isGatheringGas() && !myUnit.isCarryingGas()) {
					for (Unit materials : bwapi.getNeutralUnits()) {

						if (materials.getType().isMineralField()) {
							double distance = myUnit.getDistance(materials);
							if (distance < 300) {
								myUnit.rightClick(materials, false);
								claimedMinerals.add(materials);
								break;
							}
						}
					}
				}
			}
		}

	}

	@Override
	public void matchEnd(boolean winner) {

	}

	@Override
	public void keyPressed(int keyCode) {

	}

	@Override
	public void sendText(String text) {

	}

	@Override
	public void receiveText(String text) {

	}

	@Override
	public void playerLeft(int playerID) {

	}

	@Override
	public void nukeDetect(Position p) {

	}

	@Override
	public void nukeDetect() {

	}

	@Override
	public void unitDiscover(int unitID) {

	}

	@Override
	public void unitEvade(int unitID) {

	}

	@Override
	public void unitShow(int unitID) {

	}

	@Override
	public void unitHide(int unitID) {

	}

	@Override
	public void unitCreate(int unitID) {

	}

	@Override
	public void unitDestroy(int unitID) {

	}

	@Override
	public void unitMorph(int unitID) {

	}

	@Override
	public void unitRenegade(int unitID) {

	}

	@Override
	public void saveGame(String gameName) {

	}

	@Override
	public void unitComplete(int unitID) {

	}

	@Override
	public void playerDropped(int playerID) {

	}

	public void midgame(Player x) {
		for (Unit building : bwapi.getUnits(x)) {
			if (building.getType() == UnitType.UnitTypes.Protoss_Templar_Archives && !templararchives) {
				if (building.isBeingConstructed()) {
					System.out.println("Templar Archives check");
					templararchives = true;
				}

			}
			if (building.getType() == UnitType.UnitTypes.Protoss_Stargate && !stargate) {
				if (building.isBeingConstructed()) {
					System.out.println("Stargate check");
					stargate = true;
					break;
				}

			}
			if (building.getType() == UnitType.UnitTypes.Protoss_Fleet_Beacon && !fleetbacon) {
				if (building.isBeingConstructed()) {
					System.out.println("fleetbacon check");
					fleetbacon = true;
					break;
				}

			}
			if(building.getType()==UnitType.UnitTypes.Protoss_Photon_Cannon && !photon){
				if(building.isBeingConstructed()){
					System.out.println("Photon check");
					photon=true;
					break;
				}
			}
		}
		//if(x.getMinerals()>=150&&x.getGas()>=200) {
		//mainbuild(UnitType.UnitTypes.Protoss_Templar_Archives);
		//}
		if (!stargate && x.getMinerals() >= 150 && x.getGas() >= 150 && dragoon > 3) {
			mainbuild(UnitType.UnitTypes.Protoss_Stargate);
		}
		if (stargate && !fleetbacon && x.getMinerals() >= 600 && x.getGas() >= 300) {
			mainbuild(UnitType.UnitTypes.Protoss_Fleet_Beacon);
		}
		for (Unit units : bwapi.getUnits(x)) {
			if (units.getType() == UnitType.UnitTypes.Protoss_Stargate) {
				units.train(UnitType.UnitTypes.Protoss_Corsair);
				System.out.println("CARRIER!");
				break;
			}

			if (units.getType() == UnitType.UnitTypes.Protoss_Gateway) {
				if (x.getGas() >= 50 && x.getMinerals() >= 200) {
					units.train(UnitType.UnitTypes.Protoss_Dragoon);
					dragoon++;
					break;
				} else if (x.getMinerals() >= 450) {
					units.train(UnitType.UnitTypes.Protoss_Zealot);
					break;
				}
			}

		}
		attack();
	}


	public void earlygame(Player x) {
		if (assimilated && x.getMinerals() >= 250 && !cybercored) {
			mainbuild(UnitType.UnitTypes.Protoss_Cybernetics_Core);
			System.out.println("cybercore built");
		}
		if (building == 0) {
			for (Unit building : bwapi.getUnits(x)) {
				if (building.getType() == UnitType.UnitTypes.Protoss_Nexus && x.getMinerals() > 50 && interrupt != 0) {
					building.train(UnitType.UnitTypes.Protoss_Probe);
					break;
				}
			}
		}
		for (Unit building : bwapi.getUnits(x)) {
			if (building.getType() == UnitType.UnitTypes.Protoss_Gateway && check == 0) {
				if (building.isBeingConstructed()) {
					System.out.println("Gatewaycheck");
					check = 1;
					interrupt = 1;
					break;
				}

			}


			if (building.getType() == UnitType.UnitTypes.Protoss_Assimilator && !assimilated) {
				if (building.isBeingConstructed()) {
					System.out.println("Assimilator check");
					assimilated = true;
					break;
				}
			}
			if (building.getType() == UnitType.UnitTypes.Protoss_Cybernetics_Core && !cybercored) {
				if (building.isBeingConstructed()) {
					System.out.println("cybercore check");
					cybercored = true;
					break;
				}
			}

		}


		if (building == 1) {
			if (worker.getDistance(distance) < 50) {
				building = 2;
			}
		}
		if (building == 2) {
			counter += 1;
		}
		if (counter == 500) {
			counter = 0;
			building = 0;
			for (Unit materials : bwapi.getNeutralUnits()) {
				if (materials.getType().isMineralField()) {
					double distance = worker.getDistance(materials);
					if (distance < 500) {
						worker.rightClick(materials, false);
						claimedMinerals.add(materials);
						break;
					}
				}
			}
		}
		if (check == 0) {
			for (Unit units : bwapi.getUnits(x)) {
				if (completion == false && units.getType() == UnitType.UnitTypes.Protoss_Pylon && units.isCompleted() == true) {
					completion = true;
					System.out.println("Pylon built");
					interrupt = 0;
				}
			}
			if (x.getMinerals() >= 150 && completion == true && building == 0) {
				mainbuild(UnitType.UnitTypes.Protoss_Gateway);
				System.out.println("should build gateway");
			}
		}
		if (x.getMinerals() >= 150 && completion == true && building == 0 && check == 0) {
			mainbuild(UnitType.UnitTypes.Protoss_Gateway);
			System.out.println("WHY HERE build gateway");
		}
		if (x.getMinerals() >= 100 && !assimilated && check == 1) {
			mainbuild(UnitType.UnitTypes.Protoss_Assimilator);
			System.out.println("WHY HERE assimilator");
		}
		if (check == 1) {
			if ((gas = true && x.getMinerals() > 150) || x.getMinerals() > 400) {
				for (Unit unit : bwapi.getUnits(bwapi.getSelf())) {
					if (unit.getType() == UnitType.UnitTypes.Protoss_Gateway) {
						unit.train(UnitType.UnitTypes.Protoss_Zealot);
						break;
					}
				}
			}
		}
		attack();
	}

	public int mainbuild(UnitType x) {
		int z = 0;


		if (supply_count == 0 && completion == true) {
			z = 1;
		} else {
			z = supply_count;
		}
		for (int t = 0; t < z; t++) {
			Position pylon = (Position) pylons.get(t);
			int Start_X = pylon.getX(Position.PosType.PIXEL);
			int Start_Y = pylon.getY(Position.PosType.PIXEL);
			for (Unit builder : bwapi.getUnits(bwapi.getSelf())) {
				if (builder.getType() == UnitType.UnitTypes.Protoss_Probe) {
					for (int i = 0; i < 300; i++) {
						int Newbuilding = Start_X + x.getDimensionUp() + i;
						int Newbuilding1 = Start_X - x.getDimensionUp() - i;
						int NewBuilding2 = Start_Y + x.getDimensionUp() + 1;
						int NewBuilding3 = Start_Y - x.getDimensionUp() - 1;

						Position Newbuildingleft = new Position(Start_X, NewBuilding3);
						Position Newbuildingright = new Position(Start_X, NewBuilding2);
						Position Newbuildingup = new Position(Newbuilding, Start_Y);
						Position Newbuildingdown = new Position(Newbuilding1, Start_Y);
						worker = builder;
						building = 1;
						if (x == UnitType.UnitTypes.Protoss_Assimilator) {
							int MAX = 10;
							for (Unit n : bwapi.getNeutralUnits()) {
								if ((n.getType() == UnitType.UnitTypes.Resource_Vespene_Geyser) && (Math.abs(n.getTilePosition().getX(Position.PosType.BUILD) - bwapi.getSelf().getStartLocation().getX(Position.PosType.BUILD)) < MAX) && (Math.abs(n.getTilePosition().getY(Position.PosType.BUILD) - bwapi.getSelf().getStartLocation().getY(Position.PosType.BUILD)) < MAX)) {
									builder.build(n.getTilePosition(), UnitType.UnitTypes.Protoss_Assimilator);
									distance = n.getPosition();
									return 4;
								}
							}
						}
						if (bwapi.canBuildHere(Newbuildingleft, x, true)) {
							builder.build(Newbuildingleft, x);
							distance = Newbuildingleft;

							return 1;
						} else if (bwapi.canBuildHere(Newbuildingright, x, true)) {
							builder.build(Newbuildingright, x);
							distance = Newbuildingright;
							return 1;
						} else if (bwapi.canBuildHere(Newbuildingup, x, true)) {
							builder.build(Newbuildingup, x);
							distance = Newbuildingup;
							return 1;
						} else if (bwapi.canBuildHere(Newbuildingdown, x, true)) {
							builder.build(Newbuildingdown, x);
							distance = Newbuildingdown;
							return 1;
						}
					}

				}
			}
		}
      /*for (Unit builder : bwapi.getMyUnits()) {
         if (builder.getType() == UnitType.UnitTypes.Protoss_Probe) {
            if (location == 1 || location == 2) {
               int Pylonx = supply_pylon.getX(Position.PosType.PIXEL);
               for (int i = 0; i < 200; i++) {
                  int PylonY = supply_pylon.getY(Position.PosType.PIXEL) + UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() + i;
                  Position Newpylon = new Position(Pylonx, PylonY);
                  if (bwapi.canBuildHere(Newpylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
                     builder.build(Newpylon, UnitType.UnitTypes.Protoss_Pylon);
                     supply_pylon = Newpylon;
                     pylons.add(Newpylon);
                     return 1;
                  }
               }
            } else if (location == 3 || location == 4) {
               System.out.println(1);
               int Pylonx = supply_pylon.getX(Position.PosType.PIXEL);
               for (int i = 0; i < 200; i++) {
                  int PylonY = supply_pylon.getY(Position.PosType.PIXEL) - UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() - i;
                  Position Newpylon = new Position(Pylonx, PylonY);
                  if (bwapi.canBuildHere(Newpylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
                     builder.build(Newpylon, UnitType.UnitTypes.Protoss_Pylon);
                     supply_pylon = Newpylon;
                     pylons.add(Newpylon);
                     return 1;
                  }
               }
            }
         }*/


		return 0;
	}

	public int supply() {
		Position Start = bwapi.getSelf().getStartLocation();
		Map map = bwapi.getMap();
		Position y = map.getSize();

		int Mapx = y.getX(Position.PosType.PIXEL);
		int Mapy = y.getY(Position.PosType.PIXEL);
		int Start_X = Start.getX(Position.PosType.PIXEL);
		int Start_Y = Start.getY(Position.PosType.PIXEL);


		System.out.println("Start X" + Start_X + " " + "MapX " + Mapx);
		System.out.println("Start Y" + Start_Y + " " + "MapY " + Mapy);
		if (Math.abs(Mapx - Start_X) < Start_X) {
			if (Math.abs(Mapy - Start_Y) < Start_Y) {
				System.out.println("Bottom Right");
				location = 4;
			} else {
				System.out.println("Top Right");
				location = 1;
			}
		} else {
			if (Math.abs(Mapy - Start_Y) < Start_Y) {
				location = 3;
				System.out.println("Bottom Left");
			} else {
				location = 2;
				System.out.println("Top Left");
			}
		}

		for (Unit builder : bwapi.getUnits(bwapi.getSelf())) {
			if (builder.getType() == UnitType.UnitTypes.Protoss_Probe) {
				if (supply_pylon.getX(Position.PosType.PIXEL) == 0) {
					if (location == 2 || location == 3) {
						for (int i = 300; i < 500; i++) {
							int NewPylonX = Start_X + i;
							Position NewPylon = new Position(NewPylonX, Start_Y);
							if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
								builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
								pylons.add(NewPylon);
								supply_pylon = NewPylon;
								origin = NewPylon;
								return 1;
							}
						}
					}
					if (location == 4 || location == 1) {
						System.out.println(3);
						for (int i = 300; i < 500; i++) {
							int NewPylonX = Start_X - i;
							Position NewPylon = new Position(NewPylonX, Start_Y);
							if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
								pylons.add(NewPylon);
								builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
								supply_pylon = NewPylon;
								origin = NewPylon;
								return 1;
							}
						}
					}

				} else {
					int PylonY = supply_pylon.getY(Position.PosType.PIXEL);
					int Pylonx = supply_pylon.getX(Position.PosType.PIXEL);
					if (supply_count > 4) {
						if ((location == 1 || location == 2) && override == 0) {
							Position Pylon = origin;
							int Pylony = Pylon.getY(Position.PosType.PIXEL);
							int PylonXV = Pylon.getX(Position.PosType.PIXEL);
							for (int i = 75; i < 300; i++) {
								PylonY = origin.getY(Position.PosType.PIXEL) - UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() - i;
								Position NewPylon = new Position(PylonXV, PylonY);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									override = 1;
									runover = NewPylon;
									origin = NewPylon;
									runoverorigin = NewPylon;
									return 1;
								}
							}
						} else if ((location == 3 || location == 4) && override == 0) {
							Position Pylon = origin;
							int Pylony = Pylon.getY(Position.PosType.PIXEL);
							int PylonXV = Pylon.getX(Position.PosType.PIXEL);
							for (int i = 75; i < 300; i++) {
								PylonY = origin.getY(Position.PosType.PIXEL) + UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() + i;
								Position NewPylon = new Position(PylonXV, PylonY);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									override = 1;
									runover = NewPylon;
									origin = NewPylon;
									runoverorigin = NewPylon;
									return 1;
								}
							}
						} else if ((location == 3 || location == 4) && override == 1) {
							Position Pylon = runover;
							int Pylony = Pylon.getY(Position.PosType.PIXEL);
							int PylonXV = Pylon.getX(Position.PosType.PIXEL);
							for (int i = 0; i < 300; i++) {
								PylonY = runover.getY(Position.PosType.PIXEL) + UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() + i;
								Position NewPylon = new Position(PylonXV, PylonY);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									runover = NewPylon;
									return 1;
								}
							}

						} else if ((location == 1 || location == 2) && override == 1) {
							Position Pylon = runover;
							int Pylony = Pylon.getY(Position.PosType.PIXEL);
							int PylonXV = Pylon.getX(Position.PosType.PIXEL);
							for (int i = 0; i < 300; i++) {
								PylonY = runover.getY(Position.PosType.PIXEL) - UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() - i;
								Position NewPylon = new Position(PylonXV, PylonY);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									runover = NewPylon;
									return 1;
								}
							}
							int redoy = runover.getY(Position.PosType.PIXEL);
							int redox = runover.getX(Position.PosType.PIXEL);


						}
						int redoy = runoverorigin.getY(Position.PosType.PIXEL);
						int redox = runoverorigin.getX(Position.PosType.PIXEL);
						if (location == 1 || location == 4) {
							for (int i = 0; i < 50; i++) {
								int PylonXi = redox + UnitType.UnitTypes.Protoss_Pylon.getDimensionRight() + i;
								Position NewPylon = new Position(PylonXi, redoy);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									runover = NewPylon;
									runoverorigin = NewPylon;
									return 1;
								}
							}
						} else if (location == 2 || location == 3) {
							for (int i = 0; i < 50; i++) {
								int PylonXi = redox - UnitType.UnitTypes.Protoss_Pylon.getDimensionRight() - i;
								Position NewPylon = new Position(PylonXi, redoy);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									runover = NewPylon;
									runoverorigin = NewPylon;
									return 1;
								}
							}


						}

						if (location == 1 || location == 4) {
							for (int i = 0; i < 50; i++) {
								int PylonXi = origin.getX(Position.PosType.PIXEL) - UnitType.UnitTypes.Protoss_Pylon.getDimensionRight() - i;
								Position NewPylon = new Position(PylonXi, redoy);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									runover = NewPylon;
									runoverorigin = NewPylon;
									return 1;
								}
							}
						} else if (location == 2 || location == 3) {
							for (int i = 0; i < 50; i++) {
								int PylonXi = origin.getX(Position.PosType.PIXEL) + UnitType.UnitTypes.Protoss_Pylon.getDimensionRight() + i;
								Position NewPylon = new Position(PylonXi, redoy);
								if (bwapi.canBuildHere(NewPylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
									builder.build(NewPylon, UnitType.UnitTypes.Protoss_Pylon);
									runover = NewPylon;
									runoverorigin = NewPylon;
									return 1;
								}
							}


						}
					}


					if (location == 1 || location == 2) {
						for (int i = 0; i < 200; i++) {
							PylonY = supply_pylon.getY(Position.PosType.PIXEL) + UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() + i;
							Position Newpylon = new Position(Pylonx, PylonY);
							if (bwapi.canBuildHere(Newpylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
								builder.build(Newpylon, UnitType.UnitTypes.Protoss_Pylon);
								supply_pylon = Newpylon;
								pylons.add(Newpylon);
								return 1;
							}
						}
					} else if (location == 3 || location == 4) {
						System.out.println(1);
						for (int i = 0; i < 200; i++) {
							PylonY = supply_pylon.getY(Position.PosType.PIXEL) - UnitType.UnitTypes.Protoss_Pylon.getDimensionUp() - i;
							Position Newpylon = new Position(Pylonx, PylonY);
							if (bwapi.canBuildHere(Newpylon, UnitType.UnitTypes.Protoss_Pylon, true) == true) {
								builder.build(Newpylon, UnitType.UnitTypes.Protoss_Pylon);
								supply_pylon = Newpylon;
								pylons.add(Newpylon);
								return 1;
							}
						}
					}


				}
			}
		}
		return 0;
	}


	public Position find(int count, Unit builder) {
		Player player = bwapi.getSelf();
		int startY = 0;
		for (int i = 300; i < 500; i++) {
			int startX = player.getStartLocation().getX(Position.PosType.PIXEL);
			if (player.getStartLocation().getX(Position.PosType.PIXEL) > 3000) {
				startY = player.getStartLocation().getY(Position.PosType.PIXEL) - (count * 10);
			} else {
				startY = player.getStartLocation().getY(Position.PosType.PIXEL) - (count * 10);
			}
			if (startX > 3000) {
				startX -= i;

			} else {
				startX += i;
			}

			Position Test = new Position(startX, startY);

			if (bwapi.canBuildHere(builder, Test, UnitType.UnitTypes.Protoss_Pylon, false) == true) {

				System.out.println(i);
				return Test;
			}
		}
		return new Position(0, 0);
	}

	public void attack() {
		for (Unit unit : bwapi.getMyUnits()) {
			if (unit.getType() == UnitType.UnitTypes.Protoss_Zealot) {
				if (Zealots.contains(unit) == false && Attack.contains(unit) == false) {
					Zealots.add(unit);

					Map map = bwapi.getMap();
					List y=map.getChokePoints();
					for(Object position:y){
						position =(Position)position;
						Position start=bwapi.getSelf().getStartLocation();

						if (((Math.abs(((Position) position).getX(Position.PosType.BUILD) - bwapi.getSelf().getStartLocation().getX(Position.PosType.BUILD)) < 10) && (Math.abs(((Position) position).getY(Position.PosType.BUILD) - bwapi.getSelf().getStartLocation().getY(Position.PosType.BUILD)) < 10))) {
							Position t=(Position)position;
							unit.move(t,true);
							break;
						}

					}
				}
			}
			if (unit.getType() == UnitType.UnitTypes.Protoss_Dragoon) {
				if (Dragoon.contains(unit) == false && Attack.contains(unit) == false) {
					Dragoon.add(unit);
				}
			}
		}
		if (Zealots.size() >= 5) {
			for (int i = 0; i < (Math.round(Zealots.size() * .5)); i++) {
				Zealots.get(i);
				Attack.add(Zealots.get(i));
				Zealots.remove(i);

			}
		}
		if (Dragoon.size() >= 5) {
			for (int i = 0; i < (Math.round(Dragoon.size() * .5)); i++) {
				Dragoon.get(i);
				Attack.add(Dragoon.get(i));
				Dragoon.remove(i);
			}
		}
		for(int i=0;i<Attack.size();i++){
			Unit attacker=(Unit)Attack.get(i);
			attacker.attack(base,false);
		}
	}






	public void probe() {
		bwapi.getEnemyUnits();
		for (Unit enemies : bwapi.getEnemyUnits()) {
			if (race == 0) {
				if (enemies.getType() == UnitType.UnitTypes.Terran_Command_Center) {
					race = 1;
					base = enemies.getPosition();
				} else if (enemies.getType() == UnitType.UnitTypes.Protoss_Nexus) {
					race = 2;
					base = enemies.getPosition();
				} else if (enemies.getType() == UnitType.UnitTypes.Zerg_Hatchery) {
					race = 3;
					base = enemies.getPosition();
				}

			}
			if (race == 1) {
				if (air == false) {
					if (enemies.getType() == UnitType.UnitTypes.Terran_Starport) {
						air = true;
					}
				}
			}
			if (race == 2) {
				if (cloaked == 0) {
					if (enemies.getType() == UnitType.UnitTypes.Protoss_Dark_Templar) {
						cloaked = 1;
					}
				} else if (air == false) {
					if (enemies.getType() == UnitType.UnitTypes.Protoss_Stargate) {
						air = true;
					}
				}
			}
			if (race == 3) {
				if (air == false) {
					if (enemies.getType() == UnitType.UnitTypes.Zerg_Mutalisk) {
						air = true;
					}
				}
			}
		}
	}
}