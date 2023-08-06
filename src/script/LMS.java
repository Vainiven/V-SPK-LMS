package script;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gui.UserInterface;
import simple.api.HeadIcon;
import simple.api.actions.SimpleItemActions;
import simple.api.actions.SimplePlayerActions;
import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;
import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimplePrayers.Prayers;
import simple.api.filters.SimpleSkills.Skill;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Category;
import simple.api.script.LoopingScript;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.SimpleActor;
import simple.api.wrappers.SimplePlayer;

@ScriptManifest(author = "Vainiven", category = Category.OTHER, description = "Lms", name = "V-SPK LMS", version = "1.0", discord = "Vainiven#6986", servers = {
		"SpawnPk" })

public class LMS extends Script implements KeyListener, SimplePaintable, LoopingScript, SimpleMessageListener {

	SimplePlayer currentTargetq = null;
	int saraCount;
	static public boolean slave, started, open;
	private boolean needToDrinkRestore;
	String currentTarget;
	WorldArea fightArea = new WorldArea(new WorldPoint(3465, 5790, 0), new WorldPoint(3473, 5786, 0));
	WorldArea waitingRoom = new WorldArea(new WorldPoint(3085, 3478, 0), new WorldPoint(3089, 3469, 0));
	WorldPoint[] path = { new WorldPoint(3423, 5769, 0), new WorldPoint(3419, 5774, 0), new WorldPoint(3419, 5781, 0),
			new WorldPoint(3410, 5787, 0), new WorldPoint(3403, 5792, 0), new WorldPoint(3403, 5799, 0),
			new WorldPoint(3403, 5793, 0), new WorldPoint(3410, 5790, 0), new WorldPoint(3417, 5789, 0),
			new WorldPoint(3424, 5792, 0), new WorldPoint(3427, 5797, 0), new WorldPoint(3431, 5799, 0),
			new WorldPoint(3436, 5802, 0), new WorldPoint(3438, 5805, 0), new WorldPoint(3440, 5811, 0),
			new WorldPoint(3444, 5812, 0), new WorldPoint(3435, 5814, 0), new WorldPoint(3424, 5817, 0),
			new WorldPoint(3418, 5819, 0), new WorldPoint(3415, 5821, 0), new WorldPoint(3414, 5824, 0),
			new WorldPoint(3411, 5829, 0), new WorldPoint(3405, 5831, 0), new WorldPoint(3402, 5835, 0),
			new WorldPoint(3402, 5840, 0), new WorldPoint(3404, 5844, 0), new WorldPoint(3404, 5847, 0),
			new WorldPoint(3405, 5853, 0), new WorldPoint(3405, 5858, 0), new WorldPoint(3404, 5862, 0),
			new WorldPoint(3406, 5868, 0), new WorldPoint(3408, 5871, 0), new WorldPoint(3413, 5869, 0),
			new WorldPoint(3413, 5863, 0), new WorldPoint(3411, 5858, 0), new WorldPoint(3410, 5851, 0),
			new WorldPoint(3411, 5844, 0), new WorldPoint(3414, 5839, 0), new WorldPoint(3417, 5843, 0),
			new WorldPoint(3422, 5848, 0), new WorldPoint(3428, 5847, 0), new WorldPoint(3433, 5848, 0),
			new WorldPoint(3435, 5852, 0), new WorldPoint(3438, 5854, 0), new WorldPoint(3443, 5854, 0),
			new WorldPoint(3447, 5857, 0), new WorldPoint(3447, 5861, 0), new WorldPoint(3457, 5861, 0),
			new WorldPoint(3461, 5863, 0), new WorldPoint(3463, 5865, 0), new WorldPoint(3466, 5860, 0),
			new WorldPoint(3460, 5855, 0), new WorldPoint(3462, 5853, 0), new WorldPoint(3469, 5851, 0),
			new WorldPoint(3472, 5856, 0), new WorldPoint(3474, 5859, 0), new WorldPoint(3475, 5864, 0),
			new WorldPoint(3477, 5871, 0), new WorldPoint(3481, 5873, 0), new WorldPoint(3488, 5872, 0),
			new WorldPoint(3495, 5868, 0), new WorldPoint(3499, 5864, 0), new WorldPoint(3499, 5860, 0),
			new WorldPoint(3497, 5855, 0), new WorldPoint(3496, 5849, 0), new WorldPoint(3496, 5846, 0),
			new WorldPoint(3498, 5842, 0), new WorldPoint(3500, 5835, 0), new WorldPoint(3500, 5830, 0),
			new WorldPoint(3499, 5826, 0), new WorldPoint(3501, 5821, 0), new WorldPoint(3503, 5817, 0),
			new WorldPoint(3504, 5812, 0), new WorldPoint(3507, 5808, 0), new WorldPoint(3508, 5803, 0),
			new WorldPoint(3508, 5798, 0), new WorldPoint(3505, 5796, 0), new WorldPoint(3497, 5794, 0),
			new WorldPoint(3495, 5788, 0), new WorldPoint(3495, 5782, 0), new WorldPoint(3499, 5776, 0),
			new WorldPoint(3503, 5772, 0), new WorldPoint(3498, 5769, 0), new WorldPoint(3489, 5769, 0),
			new WorldPoint(3482, 5771, 0), new WorldPoint(3479, 5774, 0), new WorldPoint(3477, 5778, 0),
			new WorldPoint(3476, 5782, 0), new WorldPoint(3474, 5786, 0) };

	final String[] mageSet = { "Ahrim's staff", "Spirit shield", "Mystic robe bottom", "Mystic robe top",
			"Zuriel's staff" };
	final String[] rangeSet = { "Rune c'bow", "Rune platelegs", "Black d'hide body" };
	final String[] specSet = { "Dragon dagger", "Vesta longsword", "Rune platelegs", "Black d'hide body",
			"Dragon defender" };
	final String[] meleeSet = { "Abyssal whip", "Rune platelegs", "Black d'hide body", "Dragon defender" };

	@Override
	public boolean onExecute() {
		if (started) {
			return true;
		} else {
			if (!open) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							UserInterface frame = new UserInterface();
							frame.setVisible(true);
							open = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		return false;
	}

	@Override
	public void onProcess() {
		if (slave) {
			if (inLMS()) {
				eat();
				autoRetaliate();
				fightAnother();
			} else if (!inWaitingroom()) {
				clickPortal();
			}
		} else {
			drinkPrayerPotion();
			setDefensivePrayer();
			eat();
		}
	}

	@Override
	public void onTerminate() {
		slave = false;
		started = false;
		open = false;
	}

	public void clickPortal() {
		if (!ctx.objects.populate().filter("Portal").filter(new WorldPoint(3092, 3470)).isEmpty()) {
			ctx.objects.next().interact(502);
			ctx.onCondition(() -> inWaitingroom());
		} else {
			ctx.keyboard.sendKeys("::lms");
			ctx.onCondition(
					() -> !ctx.objects.populate().filter("Portal").filter(new WorldPoint(3092, 3470)).isEmpty());
		}
	}

	public boolean inLMS() {
		return ctx.players.getLocal().getLocation().getRegionID() == 13914
				|| ctx.players.getLocal().getLocation().getRegionID() == 13915
				|| ctx.players.getLocal().getLocation().getRegionID() == 13659
				|| ctx.players.getLocal().getLocation().getRegionID() == 13658;
	}

	public boolean inWaitingroom() {
		return ctx.players.getLocal().getLocation().within(waitingRoom);
	}

	public void fightAnother() {
		if (currentTargetq != null) {
			System.out.println(ctx.players.populate().filter(currentTargetq).isEmpty());
			if (ctx.players.populate().filter(currentTargetq).isEmpty() || currentTargetq.isDead()) {
				currentTargetq = null;
				currentTarget = "none";
			}
			fight(currentTargetq);
		} else if (!ctx.players.populate().isEmpty()
				&& !ctx.players.populate().filter(r -> !r.inCombat() && r.getInteracting() == null).isEmpty()) {
			System.out.println(ctx.players.next().getName());
			currentTargetq = ctx.players.next();
			currentTarget = currentTargetq.getName();
		} else {
			walkPath(path);
		}
		System.out.println(!ctx.players.populate().isEmpty());
		System.out.println(!ctx.players.populate().filter(r -> !r.inCombat() && r.getInteracting() == null).isEmpty());
	}

	private void fight(SimplePlayer target) {
		if (!target.getName().equals("YOURNAME")) {
			if (target.getOverheadIcon() == HeadIcon.MAGIC || target.getOverheadIcon() == HeadIcon.MELEE) {
				equipGear(rangeSet);
				enablePrayer(Prayers.RIGOUR);
			} else if (target.getOverheadIcon() == HeadIcon.RANGED) {
				equipGear(mageSet);
				enablePrayer(Prayers.AUGURY);
			} else if (target.distanceTo(ctx.players.getLocal()) < 1
					&& (target.getOverheadIcon() == HeadIcon.RANGED || target.getOverheadIcon() == HeadIcon.MAGIC)) {
				equipGear(meleeSet);
				enablePrayer(Prayers.PIETY);
				if (target.getHealthRatio() < 40) {
					specOverride(target);
				}
			}
			setDefensivePrayer(target);
			if (ctx.players.getLocal().getInteracting() == null) {
				target.interact(SimplePlayerActions.ATTACK);
			}
		}
	}

	public void equipGear(String[] set) {
		for (final String p : set) {
			if (!ctx.inventory.populate().filterContains(p).isEmpty()) {
				ctx.inventory.next().interact(SimpleItemActions.WEAR);
			}
		}
		if (set.equals(mageSet)) {
			// ANCIENT
			ctx.menuActions.sendAction(315, 4151, 4, 13136);
		}
		if (set.equals(specSet)) {
			enablePrayer(Prayers.PIETY);
		}
	}

	public void walkPath(WorldPoint[] path) {
		for (int i = 0; i < path.length; i++) {
			ctx.pathing.step(path[i]);
			ctx.sleep(1500);
		}
	}

	private void autoRetaliate() {
		if (!ctx.combat.autoRetaliate()) {
			ctx.combat.toggleAutoRetaliate(true);
		}
	}

	private SimpleActor<?> targetActor() {
		return ctx.players.getLocal().getInteracting();
	}

	private SimplePlayer target() {
		return ctx.players.populate().filter(targetActor()).next();
	}

	private boolean drinkPrayerPotion() {
		if (ctx.prayers.prayerPercent() < 70 || ctx.skills.getLevel(Skill.MAGIC) < 95) {
			if (!ctx.inventory.populate()
					.filterContains("Super Restore", "Sanfew serum flask", "Super restore flask", "Sanfew serum")
					.isEmpty()) {
				ctx.inventory.next().interact(SimpleItemActions.CONSUME);
			}
			return true;
		}
		return false;
	}

	public void specOverride() {
		if (!ctx.combat.specialAttack()) {
			ctx.combat.toggleSpecialAttack(true);
			System.out.println("Speccing because enemy hp is: " + target().getHealthRatio() + "%");
		}
	}

	public void specOverride(SimplePlayer target) {
		if (target.getOverheadIcon() != HeadIcon.MELEE && target.getHeadIcon() != 13
				&& ctx.combat.getSpecialAttackPercentage() >= 25) {
			if (!ctx.combat.specialAttack()) {
				ctx.combat.toggleSpecialAttack(true);
				System.out.println("Speccing because enemy hp is: " + target.getHealthRatio() + "%");
				target.interact(SimplePlayerActions.ATTACK);
				ctx.onCondition(() -> ctx.players.getLocal().getAnimation() == 1062, 20, 150);
			}
		}
	}

	private boolean eat() {
		if (saraCount == 3 || needToDrinkRestore) {
			drinkRestore();
		}
		if (ctx.combat.healthPercent() < 60 && !ctx.inventory.populate()
				.filterContains("Manta ray", "Cooked Karambwan", "Saradomin brew", "Shark").isEmpty()) {
			ctx.inventory.next().interact(SimpleItemActions.CONSUME);
			ctx.sleep(250);
			if (!ctx.inventory.populate().filterContains("Cooked Karambwan").isEmpty()) {
				ctx.inventory.next().interact(SimpleItemActions.CONSUME);
			}
			return true;
		}
		return false;
	}

	private void drinkRestore() {
		if (!ctx.inventory.populate().filterContains("Super restore").isEmpty()) {
			ctx.inventory.next().interact(SimpleItemActions.CONSUME);
			saraCount = 0;
			needToDrinkRestore = false;
			ctx.sleep(400);
		}
	}

	private void enablePrayer(Prayers prayer) {
		if (!ctx.prayers.prayerActive(prayer)) {
			ctx.prayers.prayer(prayer);
		}
	}

	private void setDefensivePrayer() {
		if (ctx.players.getLocal().getInteracting() != null) {
			if (target().getEquipment() != null) {
				int gear[] = target().getEquipment();
				if (ctx.definitions.getItemDefinition(gear[3] - 512) != null) {
					String equippedWeapon = ctx.definitions.getItemDefinition(gear[3] - 512).getName();

					String[] rangeItems = { "ballista", "blowpipe", "bow", "cannon", "knife" };
					String[] magicItems = { "korasi", "staff", "trident", "staff", "wand", "sceptre", "bulwark" };
					String[] meleeItems = { "balmiung", "godsword", "sword", "hasta", "axe", "spear", "maul", "mace",
							"rapier", "dagger", "bludgeon", "whip", "tent", "blade", "scythe", "claws", "scimitar",
							"hammer", "flail" };

					boolean isRange = containsItemName(equippedWeapon, rangeItems);
					boolean isMagic = containsItemName(equippedWeapon, magicItems);
					boolean isMelee = containsItemName(equippedWeapon, meleeItems);

					if (isRange) {
						enablePrayer(Prayers.PROTECT_FROM_MISSILES);
					} else if (isMagic) {
						enablePrayer(Prayers.PROTECT_FROM_MAGIC);
					} else if (isMelee) {
						enablePrayer(Prayers.PROTECT_FROM_MELEE);
					}
				}
			}
		}
	}

	private void setDefensivePrayer(SimplePlayer target) {
		if (ctx.players.getLocal().getInteracting() != null) {
			if (target.getEquipment() != null) {
				int gear[] = target.getEquipment();
				if (ctx.definitions.getItemDefinition(gear[3] - 512) != null) {
					String equippedWeapon = ctx.definitions.getItemDefinition(gear[3] - 512).getName();

					String[] rangeItems = { "ballista", "blowpipe", "bow", "cannon", "knife" };
					String[] magicItems = { "korasi", "staff", "trident", "staff", "wand", "sceptre", "bulwark" };
					String[] meleeItems = { "balmiung", "godsword", "sword", "hasta", "axe", "spear", "maul", "mace",
							"rapier", "dagger", "bludgeon", "whip", "tent", "blade", "scythe", "claws", "scimitar",
							"hammer", "flail" };

					boolean isRange = containsItemName(equippedWeapon, rangeItems);
					boolean isMagic = containsItemName(equippedWeapon, magicItems);
					boolean isMelee = containsItemName(equippedWeapon, meleeItems);

					if (currentTargetq.getName().equals("Qazzy") || currentTargetq.getName().equals("Mooiboi")
							|| currentTargetq.getName().equals("Pliiynik")
							|| currentTargetq.getName().equals("Certoyu")) {
						ctx.sleep(2000, 4500);
						if (isRange) {
							enablePrayer(Prayers.PROTECT_FROM_MISSILES);
						} else if (isMagic) {
							enablePrayer(Prayers.PROTECT_FROM_MAGIC);
						} else if (isMelee) {
							enablePrayer(Prayers.PROTECT_FROM_MELEE);
						}
					}
				}
			}
		}
	}

	private boolean containsItemName(String item, String[] itemNames) {
		for (String i : itemNames) {
			if (item.toLowerCase().contains(i)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!slave) {
			final int key = e.getKeyCode();
			switch (key) {
			case KeyEvent.VK_Q: {
				System.out.println("We have detected Q. Mage Set.");
				ctx.keyboard.pressKey(KeyEvent.VK_BACK_SPACE);
				enablePrayer(Prayers.AUGURY);
				equipGear(mageSet);
				break;
			}

			case KeyEvent.VK_W: {
				System.out.println("We have detected W. Range Set.");
				ctx.keyboard.pressKey(KeyEvent.VK_BACK_SPACE);
				enablePrayer(Prayers.RIGOUR);
				equipGear(rangeSet);
				break;
			}

			case KeyEvent.VK_E: {
				System.out.println("We have detected E. Melee Set.");
				ctx.keyboard.pressKey(KeyEvent.VK_BACK_SPACE);
				enablePrayer(Prayers.PIETY);
				equipGear(meleeSet);
				specOverride();
				break;
			}

			case KeyEvent.VK_R: {
				System.out.println("We have detected E. Spec Set.");
				ctx.keyboard.pressKey(KeyEvent.VK_BACK_SPACE);
				enablePrayer(Prayers.PIETY);
				equipGear(specSet);
				specOverride();
				break;
			}

			default:
				break;
			}

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("We are a slave: " + slave, 15, 100);
		g.drawString("In waiting room: " + inWaitingroom(), 15, 90);
		g.drawString("In LMS: " + inLMS(), 15, 80);
		g.drawString(currentTarget != null && slave ? "Slave target:" + currentTarget : "Slave target: none", 15, 110);
	}

	@Override
	public int loopDuration() {
		return 200;
	}

	@Override
	public void onChatMessage(ChatMessageEvent message) {
		if (message.getMessage().contains("You need at least level 94 Magic to cast this spell!")) {
			needToDrinkRestore = true;
		}
	}

}
