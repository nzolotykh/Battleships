package zolotykh_CSCI201_Assignment4;

public class WaterThread extends Thread {
	//responsible for the background water animation
	//alternates background images for all labels for the
	//computer and player fields. Also alternates the images
	//between odd and even labels (so adjacent labels should
	//have different images to make the fields look better)
	
	boolean status = true; //used to alternate images
	JLabelWithBackground label1;
	JLabelWithBackground label2;
	private GUI gui;
	WaterThread (GUI gui) {
		this.gui = gui;
	}
	
	public void run() {
		if (status) {
			status = false;
			for (int i = 11; i < 121; i++) {
				if (i%11 == 0) {
					//not the field
				} else {
					//field
					label1 = (JLabelWithBackground) gui.playerLabels[i]; //player side
					label2 = (JLabelWithBackground) gui.computerLabels[i]; //computer side
					if (i%2 == 0) {
						//even labels
						label1.changeBackground(gui.labelBackground1);
						label2.changeBackground(gui.labelBackground1);
					} else {
						//odd labels
						label1.changeBackground(gui.labelBackground2);
						label2.changeBackground(gui.labelBackground2);
					}
				}
			}
		} else {
			status = true;
			for (int i = 11; i < 121; i++) {
				if (i%11 == 0) {
					//not the field
				} else {
					//field
					label1 = (JLabelWithBackground) gui.playerLabels[i]; //player side
					label2 = (JLabelWithBackground) gui.computerLabels[i]; //computer side
					if (i%2 == 0) {
						//even labels
						label1.changeBackground(gui.labelBackground2);
						label2.changeBackground(gui.labelBackground2);
					} else {
						//odd labels
						label1.changeBackground(gui.labelBackground1);
						label2.changeBackground(gui.labelBackground1);
					}
				}
			}
		}
		try {
			//animation goes on...
			sleep(2500);
			run();
		} catch (InterruptedException ex) {ex.getStackTrace();}
	}
}
