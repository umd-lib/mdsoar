# MD-SOAR Theme Customization
MD-SOAR is configured to use Mirage2 as its default theme and each of the communities in mdsoar are configured to use a custom theme that inherits from the xmlui-mirage2 module.

## Global Theme Customization
All global theme customizations has to go into the [xmlui-mirage2](../modules/xmlui-mirage2) module (`dspace/modules/xmlui-mirage2/src/main/webapp/themes/Mirage2`).

### CSS
Global css overrides and new styles that needs to inherited by community themes should be added to `_global.scss` instead of the `_style.scss`.

Overrides and new styles added to `_style.scss` in Mirage2 module will not be included in the community themes.

### XSL
Global page layout changes shold be made in the xsl section of the Mirage2 project.

Community themes overrides are at the file level. To override part of a xsl, move the section to a separate file and import it into the main file. See `footer.xsl` and `page_structure.xsl` for example.

## Community Theme Customization
Changes specific to the community themes has to go into [xmlui-mirage2-communities](../modules/xmlui-mirage2-communities) module (`dspace/modules/xmlui-mirage2-communities/src/main/webapp/themes/<THEME_NAME>`).

### CSS
Overrides and new styles shoule be added to `_style.scss` file and it will override any global styles defined in the Mirage2 theme. Other style files can be copied from the Mirage2 theme for overriding in the community theme.

**NOTE**: `_global.scss` should not be added to the community themes'. This will prevent any global customization from being inherited by the community theme.

### XSL
Community overrides of the page layout has to go in to the xsl folder of that particular community theme. Any file in this directory will have precedence over the Mirage2 xsl files.

## Adding New Community Theme
To add a new community theme:

* Copy an existing community theme from `dspace/modules/xmlui-mirage2-communities/src/main/webapp/themes/` with the new community's name.
* Change the community logo in the images directory.
* Change the logo url in the `xsl\trail.xsml` to point the new community's handle.
* Change the footer text in the `xsl/footer.xsl`.
* Configure the `dsapce/config/xmlui.xconf` to use the new theme for the respective community handle.