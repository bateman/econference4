# Introduction #

This page lists all the **good practices** _(to be respected at any cost)_ and **bad practices** _(to be avoided at any cost)_ that must be observed by any developer contributing to the eConference 4 project.

Besides, you may want to follow this [guide](http://eclipse.dzone.com/articles/using-mylyn-with-google-code-u) to configure Eclipse to display and manage the issue from the IDE.


# Details #

### Do's ###
  1. report issue description in English (not Italian!)
  1. report number and link to the issue tracker item when committing a bug fix, or enhancement, or whatever change made in response to an open issue
  1. respect Java coding style and conventions for vars, files, packages, classes, interfaces, methods and so on (see [here](http://www.oracle.com/technetwork/java/codeconv-138413.html) for more)
  1. use png images whenever possible, gif otherwise;
  1. when a bug is encountered, first open an issue in the issue tracker and describe it; then fix it and commit changes
  1. write JUnit/Mockito (if possible) test cases for new features added (please, read these guides about [unit](HowToWriteJunitTestCases.md) and [mock](HowToWriteMockTestCases.md) testing for more)
  1. After writing your own tests, make sure you re-run them with all the others (look at the classes within the package `it.uniba.di.cdg.econference-tests`) to check you did not add any regression to the code base
  1. add the MIT FOSS License prologue when adding new files to the repository. Grab a copy of the MIT License [here](MitLicense.md)
  1. ensure that the changes applied don't break the export product procedure (see [here](http://code.google.com/p/econference4/wiki/howtobuild#Exporting_the_product) for more on how to export the eConference product)
  1. new builds uploaded to the [Downloads](http://code.google.com/p/econference4/downloads/list) section must respect this naming conventions
    * `econference-win32-4.x.y.zip`
    * `econference-win32-4.x.y-MMDDYYYY.zip` (if the build is unstable)
    * `econference-osx-4.x.y.zip`
    * `econference-linux-4.x.y.tgz`
  1. whenever a new stable release is uploaded, its changes must be appropriately described in the [changelog.txt](http://code.google.com/p/econference4/source/browse/trunk/it.uniba.di.cdg.collaborativeworkbench.boot/changelog.txt) file within the repository.

### Dont's ###
  1. add code comments in Italian! Use English, instead.
  1. commit broken files
  1. commit untested files
  1. commit unformatted files, use Eclipse built-in style
  1. commit files with warnings (e.g. unused vars or imports)
  1. commit files with unorganized imports
  1. commit files with `//XXX`, `//TODO`, and `//FIXME` comment annotations that do not have a related issue open in the issue tracker
  1. commit files with unused vars or code blocks
  1. commit any resource without a description or changelog in the comment field
  1. commit changes without any related JUnit/Mockito test cases (if possible), to prove that everything works fine
  1. commit images whose file name contains capital letter (e.g. use `agenda_item.png` rather than `agendaItem.PNG`)