================================
ExternalFinder plugin for OmegaT
================================

ExternalFinder is a plugin for OmegaT designed to make translation easy.

This plugin allows a user to query selected words using a user-specified web service (e.g. Google) or executable (e.g. an dictionary application).

The latest release and downloading a runnable file
--------------------------------
https://github.com/hiohiohio/omegat-plugin-externalfinder/releases/latest

Requirements
--------------------------------
OmegaT v2.6.3 or later

Installation
--------------------------------
* Download a zip file from the above page.
* Unzip it.
* Put OmegaT-plugin-ExternalFinder.jar on the plugins directory of OmegaT.
* Put finder.xml on user's config directory or a project directory.
* **For Mac users**: please run the following command from terminal to allow this plugin can execute commands. ***chmod +x /Applications/OmegaT.app/Contents/MacOS/jre/lib/jspawnhelper***

Configuration
--------------------------------
Put a XML file called **finder.xml** on your omegat project's root directory (as the same location as omegat.project is in) or your omegat's user config directory (please see the table below). The file is loaded when OmegaT loads a project. The result builds menu items under **Tools** menu and right-click context menu with *selected* text. If the same name is defined on the two separate files (the user's config directory and a project directory), the values of a project directory always overwrite the values of user's config directory based on **name** element.

Note: User's config directory quoted from the [Manual](http://sourceforge.net/p/omegat/code/ci/master/tree/docs/en/chapter.files.and.folders.html).

|OS|Path|
|---|---|
|Windows 2000 and XP|Documents and Settings\\&lt;User Name&gt;\\Application Data\\OmegaT|
|Windows Vista and 7|Users\\&lt;User Name&gt;\\AppData\\Roaming\\OmegaT|
|Windows other|&lt;Something&gt;\\OmegaT (&lt;Something&gt; corresponds to the location of the "home" folder as determined by Java)|
|Linux/Solaris/FreeBSD|&lt;User Home&gt;/.omegat (.omegat is a folder, the dot preceding its name makes it invisible unless you type ls -a or an equivalent command)|
|MAC OS X|&lt;User Home&gt;/Library/Preferences/OmegaT|
|Other|&lt;User Home&gt;|
	
#### finder.xml 

* **name**: A string to show as a menu item.
* **url**: A URL with a **{target}** placeholder with *optional* attributes: *target* and *encoding*.
* **command**: A command string with a **{taget}** placeholder with *optional* attributes: *target*, *encoding*, and *delimiter*.
* **keystroke**: A definition of the keyboard shortcut (same as OmegaT's custom keyboard shortcut). Ref. [KeyStroke (Java Platform SE 8 )](http://docs.oracle.com/javase/8/docs/api/javax/swing/KeyStroke.html#getKeyStroke-java.lang.String-)

#### finder.xml examples:

* The value of **target** attribute can be **both**, **ascii_only**, or **non_ascii_only**.
* The value of **encoding** attribute can be **default**, **escape**, or **none**. The difference between *default* and *escape* is the whitespace which becomes **+** with *default* (URL Encoding) and **%20** with *escape* (for some web sites).
* The default values of *target* and *encoding* attributes are **both** and **default**.

```
<?xml version="1.0" encoding="UTF-8" ?>
<items>
    <item>
        <name>Google</name>
        <url target="both" encoding="default">https://www.google.com/search?q={target}</url>
        <url target="ascii_only">https://www.google.com/search?q=define%3A{target}</url>
        <keystroke>ctrl shift F</keystroke>
    </item>
    <item>
        <name>Yahoo</name>
        <url target="ascii_only">http://search.yahoo.com/search?p={target}</url>
        <url target="non_ascii_only">http://search.yahoo.co.jp/search?p={target}</url>
    </item>
</items>
```

* This plugin can call executables using **command** element. The default values of target and encoding attributes are **both** and **none**.
* The **delimiter** attribute for **command** element is used to define the delimiter for parameters. The default *delimiter* is **|**.

```
<?xml version="1.0" encoding="UTF-8" ?>
<items>
    <item>
        <name>Dictionary</name>
        <command encoding="default">/usr/bin/open|dict://{target}</url>
        <keystroke>ctrl shift K</keystroke>
    </item>
</items>
```

Licensing
--------------------------------
Please see the file called LICENSE.

Other plugins
--------------------------------
A list of plugins on [http://hiohiohio.github.io/](http://hiohiohio.github.io/)

Acknowledgements
--------------------------------
The OmegaT project ([http://www.omegat.org/](http://www.omegat.org/)) and all contributors.
