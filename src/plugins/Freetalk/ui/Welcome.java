/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package plugins.Freetalk.ui;

import plugins.Freetalk.Freetalk;
import freenet.support.HTMLNode;

public class Welcome {
	
	public static String makeWelcomePage(Freetalk ft) {
		HTMLNode pageNode = ft.getPageNode();
		HTMLNode contentNode = ft.mPageMaker.getContentNode(pageNode);
		contentNode.addChild(createWelcomeBox(ft));
		contentNode.addChild(createOverviewBox(ft));
		/* FIXME: the backup stuff is broken. repair it or get rid of it. i think we should rather autobackup or whatever.
		contentNode.addChild(createBackupHintBox(ft)); 
		*/
		return pageNode.generate();
	}
	
	private static HTMLNode createWelcomeBox(Freetalk ft) {
		HTMLNode welcomeBox = ft.mPageMaker.getInfobox("Welcome");
		HTMLNode welcomeContent = ft.mPageMaker.getContentNode(welcomeBox);
		welcomeContent.addChild("P", "Welcome to GenTec Labs. This is our last experiment: cloning fms.");
		welcomeContent.addChild("P", "Things happens you didn't expect? Call 0800-GordonFreeman for rescue");
		return welcomeBox;
	}

	private static HTMLNode createOverviewBox(Freetalk ft) {
		HTMLNode overviewBox = ft.mPageMaker.getInfobox("Overview");
		HTMLNode overviewContent = ft.mPageMaker.getContentNode(overviewBox);
		HTMLNode list = overviewContent.addChild("ul");
		list.addChild(new HTMLNode("li", "Own Identities: " + ft.countOwnIdentities()));
		list.addChild(new HTMLNode("li", "Known Identities: " + ft.countIdentities()));
		return overviewBox;
	}

	private static HTMLNode createBackupHintBox(Freetalk ft) {
		HTMLNode bhBox = ft.mPageMaker.getInfobox("The boring backup reminder");
		HTMLNode bhContent = ft.mPageMaker.getContentNode(bhBox);
		bhContent.addChild("P", "You can not turn me off, because I'm boring. :P");
		bhContent.addChild("P", "Don't forget to backup your data. You find the buttons below.");
		bhContent.addChild(createExportBox(ft));
		bhContent.addChild(createImportBox(ft));
		return bhBox;
	}
	
	private static HTMLNode createExportBox(Freetalk ft) {
		HTMLNode exportBox = ft.mPageMaker.getInfobox("Export");
		HTMLNode exportContent = ft.mPageMaker.getContentNode(exportBox);
		HTMLNode exportForm = ft.mPluginRespirator.addFormChild(exportContent, Freetalk.PLUGIN_URI + "/exportDB", "exportForm");
		exportForm.addChild("#", "Export the database (Identities etc pp) to xml file. \u00a0 ");
		exportForm.addChild("input", new String[] { "type", "name", "value" }, new String[] { "submit", "export", "Export" });
		exportForm.addChild("br");
		exportForm.addChild("#", "Store the backup at a safe place. You can reimport it at any node and continue flaming...");
		return exportBox;
	}
	
	private static HTMLNode createImportBox(Freetalk ft) {
		HTMLNode importBox = ft.mPageMaker.getInfobox("Import");
		HTMLNode importContent = ft.mPageMaker.getContentNode(importBox);
		HTMLNode importForm = ft.mPluginRespirator.addFormChild(importContent, Freetalk.PLUGIN_URI + "/importDB", "importForm");
		importForm.addChild("#", "Choose xml file to import.\u00a0");
		importForm.addChild("input", new String[] { "type", "name", "value" }, new String[] { "file", "filename", "" });
		importForm.addChild("#", "\u00a0");
		importForm.addChild("input", new String[] { "type", "name", "value" }, new String[] { "submit", "import", "Import" });
		importForm.addChild("br");
		importForm.addChild("#", "You should only try to import files that was exported with the function above. Otherwise call the rescue number.");
		return importBox;
	}
}