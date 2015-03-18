

# How to build eConference 4.x #
This guide has been tested on Windows and Ubuntu boxes.

## Plugins Needed ##
eConference is built on the Eclipse RCP 3.6.x (_NOTE: we don't ensure that the build process is going to work with any other earlier versions_), so you will need to download and install [Eclipse](http://www.eclipse.org/downloads/) and 32bit [Java 6+](http://java.sun.com/javase/downloads/index.jsp) virtual machine. eConference also uses AOP, so you will need to install the [AJDT](http://www.eclipse.org/ajdt/) plugin for Eclispe as well. You had better install it from the Eclipse Update Manager.
For further info, please refer to the project [homepage](http://code.google.com/p/econference/).

Now run Eclipse and make sure you start using an empty workspace (see Fig. 1). If you are already running Eclipse, instead, go to the _File | Switch workspace" menu._

<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/workspace.JPG'>http://econference4.googlecode.com/svn/wiki/img/workspace.JPG</a> <br>
<p align='center'><b>Figure 1. Select a clean workspace for the eConference project</b></p>
<br>
<br>

<h2>Checkout the SVN repository</h2>
Even if you don't have a Gmail account registered to Google Code, you can check out the source code from our SVN repository directly from within Eclipse. You will need either <a href='http://subclipse.tigris.org/install.html'>Subclipse</a> or <a href='http://www.eclipse.org/subversive/downloads.php'>Subversive</a> installed. Again, it is recommended to install it from the Update Manager.<br>
<br>
Once you have installed it, go to the <i>Windows | Preferences...</i> menu, then select <i>SVN</i> under the <i>Team</i> branch (see Fig 2).<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/svn.JPG'>http://econference4.googlecode.com/svn/wiki/img/svn.JPG</a> <br>
<p align='center'><b>Figure 2. Make sure to select 'SVNKit' (Pure Java) interface</b></p>
<br>
<br>

After that, open the <i>Window | Open perspective</i> menu (see Fig. 3)...<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/svn2.JPG'>http://econference4.googlecode.com/svn/wiki/img/svn2.JPG</a> <br>
<p align='center'><b>Figure 3. Display all the available perspectives</b></p>
<br>
<br>

...and select <i>SVN Repository Exploring</i> (see Fig. 4)<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/svn3.JPG'>http://econference4.googlecode.com/svn/wiki/img/svn3.JPG</a> <br>
<p align='center'><b>Figure 4. Add the SVN Repository Exploring perspective to the workbench</b></p>
<br>
<br>

After switching to the <i>SVN Repository Exploring</i> perspective, add a new repository as shown below (Fig. 5).<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/svn4.JPG'>http://econference4.googlecode.com/svn/wiki/img/svn4.JPG</a> <br>
<p align='center'><b>Figure 5. Add the SVN Repository Location</b></p>
<br>
<br>

Now, enter <code>http://econference4.googlecode.com/svn/trunk/</code> in the url field of the wizard and click the <i>Finish</i> button. Accept the Digital Certificate when prompted.<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/svn5.JPG'>http://econference4.googlecode.com/svn/wiki/img/svn5.JPG</a> <br>
<p align='center'><b>Figure 6. URL of the remote SVN Repository Location</b></p>
<br>
<br>

Once Subclipse has finished fetching data from the new repository, you will see the repository name show up in the tree table viewer on the left pane (Fig. 7). Browse <i>Trunk</i>, then select all the plugins and right-click and choose <i>Checkout...</i> from the menu.<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/svn6.JPG'>http://econference4.googlecode.com/svn/wiki/img/svn6.JPG</a> <br>
<p align='center'><b>Figure 7. Checking out the projects into the workspace</b></p>
<br>
<br>

A 2-steps wizard will guide you. You are going to checkout each plugin as a separate project into your workspace. It could take a while, so be patient.<br>
<br>
<h2>What if I don't want to install Subclipse?</h2>
Then, you can access repository using other SVN tool, like <a href='http://tortoisesvn.tigris.org/'>Tortoise SVN</a>. Besides, you can als download the latest tarball of the source code from <a href='http://cde.di.uniba.it/snapshots.php?group_id=9'>here</a>. Now extract somewhere the tgz file (if you are running a Win OS, make sure to extract it in locations like <code>DRIVE_LETTER:</code> (e.g.,<code>C:</code> or <code>D:</code>), otherwise you'll get errors due to maximum path-lenght limitations).<br>
<br>
Assuming you have either extracted the content in the root dir (you should have a new dir like this: <code>C:/econference-scm-yyyy-mm-ddtrunk</code>) or checked out the source with a SVN tool somewhere else: All the folders within (except for <code>.svn</code>) are plugins (they are 23 as of this writing) that must be imported into your workspace as separate projects. Do this, then you are ready to build the workspace.<br>
<br>
Otherwise, you can always <a href='https://cde.di.uniba.it/plugins/scmsvn/viewcvs.php/?root=econference'>browse the code online</a> :D<br>
<br>
<br>

<h2>Building the workspace</h2>
Once either the checkout or the import is complete, you can build your workspace, but just make sure that the Java compiler compliance level is set to 1.6 (see Fig. 8)<br>
<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/javasettings.JPG'>http://econference4.googlecode.com/svn/wiki/img/javasettings.JPG</a> <br>
<p align='center'><b>Figure 8. Set the Java Compiler Compliance Level to 1.6</b></p>
<br>
<br>

<h2>Launching the product</h2>
To launch the tool for the first time, open the <code>collaborativeworkbench.boot</code> project (see Fig. 9, A) and open the product file (B). Then just click on the <i>Launch the product</i> link in the <i>Overview</i> tab (C).<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/launchingproduct.JPG'>http://econference4.googlecode.com/svn/wiki/img/launchingproduct.JPG</a> <br>
<p align='center'><b>Figure 9. Launching the product from Eclipse</b></p>
<br>
<br>

If everthing is fine, you should see the tool splashscreen come up. Otherwise, you'll get a popup with error. Sometimes, on some machine, the tool doesn't start because it misses some of the plugins/projects that are supposed to be already in the classpath. To fix this issue, go to the <i>Run | Run ...</i> menu and select the current product configuration under the <i>Eclipse Application</i> (Fig. 10, A). Then press the <i>Validate Plug-in Set</i> button (B). If an error message pops up, then close it and press the <i>Add Required Plug-ins</i> button (C); otherwise here you are already ok (and the problem should be somewhere else :S).<br>
<br>
From now on, you can just run the tool the way you usually do in Eclipse.<br>
<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/runconf.JPG'>http://econference4.googlecode.com/svn/wiki/img/runconf.JPG</a> <br>
<p align='center'><b>Figure 10. Validate the plug-ins set</b></p>
<br>
<br>


<h2>Exporting the product</h2>
Of course, you need Eclipse only to build the tool. But if you also want to run the tool as a standalone application, then you need to export the product to your filesystem.<br>
<br>
Again, open the <code>collaborativeworkbench.boot</code> project and open the product file. After that, just click on the <i>Eclipse Product export wizard</i> link in the <i>Overview</i> tab (Fig. 11).<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/export.JPG'>http://econference4.googlecode.com/svn/wiki/img/export.JPG</a> <br>
<p align='center'><b>Figure 11. Launch the Eclipse Product export wizard</b></p>
<br>
<br>


Now, as shown in Fig. 12, just choose the <i>Root directory</i> name (e.g. econference), which will be the root folder containing all the stuff exported, and the destination directory (e.g. <code>C:\myproduct</code>).<br>
<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/export2.JPG'>http://econference4.googlecode.com/svn/wiki/img/export2.JPG</a> <br>
<p align='center'><b>Figure 12. Choose the destination directory to export to</b></p>
<br>
<br>

Leave all the other options unchanged. Press finish and wait: you will find the tool exported in the choosen path (in this case, <code>C:\myproduct\econference</code>). Done.<br>
<br>
<br>

<h3>Adding Skype support under Linux boxes</h3>
While eConference works fine with Skype on Windows out of the box, you need a couple of extra steps for a proper configuration.<br>
TBD.<br>
<br>
<br>


<h2>Code & Comments style</h2>
Last but not least, be also sure to stick on the same coding style that has been used so far.<br>
<br>
Provided that you have succesfully configured and built the whole workspace, download <a href='http://cdg.di.uniba.it/uploads/Research/econf_style.zip'>this archive</a>, and finally import the following XML files, <code>codestyle.xml</code> and <code>codetemplates.xml</code> in Eclipse. To do so, just go in the <i>Windows | Preferences...</i> menu, the browse the tree viewer and select <i>Code Style</i> under the <i>Java</i> branch. Import the two XML files in the proper page (see Fig. 15).<br>
<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/formatter.JPG'>http://econference4.googlecode.com/svn/wiki/img/formatter.JPG</a> <br>
<p align='center'><b>Figure 15. Import code template and formatter style for the eConference project</b></p>
<br>
<br>


<h2>Troubleshooting</h2>

<h3><i>- I'm getting stack map versioning errors with JRE7...</i></h3>
Add -XX:-UseSplitVerifier as VM argument in the run configuration<br>
<br>
<h3><i>- Can't build the workspace without errors</i></h3>
So you have set the Java compiler compliance level to 1.6, but still get annoying red crosses everywhere in the workspace, uh?<br>
<br>
Well most of the times they are due to the the 'AspectJ' Compiler. To get rid of them, you should try to clean the workspace and rebuild it from scratch.<br>
<br>
Also disabling the automatic build of the workspace is not a bad idea, as AJDT starts a new build too often and slows you down.<br>
<br>
<br>
<br>
If you have errors in a few projects only (two or three), try to close and re-open those projects, then right-click on each of them and select <i>Build project</i> from the context menu.<br>
<br>
<br>


<h3><i>- Can't see my changes when I build and run</i></h3>
In this case, you should just try to add the <code>-clean</code> argument when you run the tool.<br>
<br>
Go to the <i>Run | Run Configurations ...</i> menu and select the current product configuration under the <i>Eclipse Application</i> (Fig. 16). Then append <code>-clean</code> to the <i>Program Arguments</i> field.<br>
<br>
This does the trick for Eclipse, and so it is supposed to do with eConference...<br>
<br>
<br>
<p align='center'><a href='http://econference4.googlecode.com/svn/wiki/img/clean.JPG'>http://econference4.googlecode.com/svn/wiki/img/clean.JPG</a> <br>
<p align='center'><b>Figure 16. Adding <code>-clean</code> as program argument does the trick sometimes...</b></p>


Please report comments or suggestions sending an email to one of the project owners. Thanks.