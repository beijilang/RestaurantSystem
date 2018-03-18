package backend.inventory;

/**
 * InventoryIngredient represents the Ingredient in backend.inventory
 *
 * <p>InventoryIngredient methods include checking whether the ingredient in the restaurant
 * backend.inventory is below the lower threshold
 */
public class InventoryIngredient extends Ingredient {
  private int lowerThreshold;
  private int mirrorQuantity;
  private boolean isUnderThreshold;
  private boolean wouldBeUnderThreshold;


  /**
   * Constructor for InventoryIngredient that takes in the name, quantity and lower threshold for
   * the ingredient
   *
   * @param name the name of this InventoryIngredient
   * @param quantity the quantity of this InventoryIngredient in stock
   * @param lowerThreshold the lowerThreshold of this InventoryIngredient; if the quantity of this
   *     Ingredient goes below this lowerThreshold, then a restock order will be made
   */
  public InventoryIngredient(String name, int quantity, int lowerThreshold) {
    super(name, quantity);
    this.lowerThreshold = lowerThreshold;
    this.mirrorQuantity = quantity;
    this.isUnderThreshold = quantity < lowerThreshold;
  }

  /**
   * Returns true iff the quantity of this InventoryIngredient is below the lower threshold
   *
   * @return boolean statement
   */
  public boolean isLowStock() {
    return this.lowerThreshold > this.getQuantity();
  }

  @Override
  public void modifyQuantity(int quantityUnit) {
    boolean bool1 = this.isUnderThreshold;
    super.modifyQuantity(quantityUnit);

    if ((this.getQuantity() < lowerThreshold) && (!bool1)) {
      modifyIsUnderThreshold(true);
    } else if (((this.getQuantity() >lowerThreshold) && (bool1))) {
      modifyIsUnderThreshold(false);
    }
  }

  public void modifyMirrorQuantity(int quantityUnit) {
      boolean bool1 = this.wouldBeUnderThreshold;
      super.modifyQuantity(quantityUnit);

      if ((this.mirrorQuantity < lowerThreshold) && (!bool1)) {
          modifyIsUnderThreshold(true);
      } else if ((this.mirrorQuantity > lowerThreshold) && (bool1)) {
          modifyIsUnderThreshold(false);
      }
  }

  private void modifyIsUnderThreshold(boolean bool) {
    this.isUnderThreshold = bool;
  }

  public boolean getIsUnderThreshold() {
    return this.isUnderThreshold;
  }

  public boolean getWouldBeUnderThreshold() {
    return this.wouldBeUnderThreshold;
  }



}