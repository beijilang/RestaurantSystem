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
    this.wouldBeUnderThreshold = this.isUnderThreshold;
  }

  /**
   * Returns true iff the quantity of this InventoryIngredient is below the lower threshold
   *
   * @return boolean statement
   */
  public boolean isLowStock() {
    return this.lowerThreshold > this.getQuantity();
  }

  public int getMirrorQuantity() {
    return this.mirrorQuantity;
  }

  /**
   * Modifies the quantity of this InventoryIngredient. If the quantityUnit is positive, increase
   * the quantity of this InventoryIngredient by the quantity. If the quantityUnit is negative, then
   * decrease the quantity of this InventoryIngredient by the quantity. If the quantity goes below
   * or above the the lower threshold, then the IsUnderThreshold will be turned into True or False,
   * respectively.
   *
   * @param quantityUnit the quantity of the ingredient that must be added or removed
   */
  @Override
  public void modifyQuantity(int quantityUnit) {
    boolean bool1 = this.isUnderThreshold;
    super.modifyQuantity(quantityUnit);

    if ((this.getQuantity() < lowerThreshold) && (!bool1)) {
      modifyIsUnderThreshold(true);
    } else if (((this.getQuantity() > lowerThreshold) && (bool1))) {
      modifyIsUnderThreshold(false);
    }
  }

  /**
   * Modifies the quantity of this InventoryIngredient. If the quantityUnit is positive, increase
   * the quantity of this InventoryIngredient by the quantity. If the quantityUnit is negative, then
   * decrease the quantity of this InventoryIngredient by the quantity. If the quantity goes below
   * or above the the lower threshold, then the IsUnderThreshold will be turned into True or False,
   * respectively.
   *
   * @param quantityUnit the quantity
   */
  public void modifyMirrorQuantity(int quantityUnit) {
    boolean bool1 = this.wouldBeUnderThreshold;
    this.mirrorQuantity += quantityUnit;

    if ((this.mirrorQuantity < lowerThreshold) && (!bool1)) {
      modifyWouldBeUnderThreshold(true);
    } else if ((this.mirrorQuantity > lowerThreshold) && (bool1)) {
      modifyWouldBeUnderThreshold(false);
    }
  }

  /**
   * Modifies the IsUnderThreshold variable of this InventoryIngredient to bool
   *
   * @param bool whether the isUnderThreshold must be converted into True or False
   */
  private void modifyIsUnderThreshold(boolean bool) {
    this.isUnderThreshold = bool;
  }

  /**
   * Modifies the WouldBeUnderThreshold variable of this InventoryIngredient to bool
   *
   * @param bool whether the WouldBeUnderThreshold must be converted into True or False
   */
  private void modifyWouldBeUnderThreshold(boolean bool) {
    this.wouldBeUnderThreshold = bool;
  }

  /**
   * Returns the IsUnderThreshold of this InventoryIngredient
   *
   * @return the IsUnderThreshold of this InventoryIngredient
   */
  public boolean getIsUnderThreshold() {
    return this.isUnderThreshold;
  }

  /**
   * Returns the WouldBeUnderThreshold of this InventoryIngredient
   *
   * @return the WouldBeUnderThreshold of this InventoryIngredient
   */
  public boolean getWouldBeUnderThreshold() {
    return this.wouldBeUnderThreshold;
  }
}
