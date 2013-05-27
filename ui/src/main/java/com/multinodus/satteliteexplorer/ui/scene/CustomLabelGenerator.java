package com.multinodus.satteliteexplorer.ui.scene;

/**
 * Created with IntelliJ IDEA.
 * User: т
 * Date: 27.05.13
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */

import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;

import java.text.AttributedString;

/**
 * A custom label generator (returns null for one item as a test).
 */
class CustomLabelGenerator implements PieSectionLabelGenerator {
  public String generateSectionLabel(PieDataset pieDataset, Comparable comparable) {
    return comparable.toString() + " = " + pieDataset.getValue(comparable).toString();
  }

  @Override
  public AttributedString generateAttributedSectionLabel(PieDataset pieDataset, Comparable comparable) {
    return new AttributedString(comparable.toString() + " = " + String.format("%.2f", (int)pieDataset.getValue(comparable)));
  }
}