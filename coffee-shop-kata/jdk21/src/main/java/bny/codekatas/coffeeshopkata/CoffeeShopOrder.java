/*
 * Copyright 2024 The Bank of New York Mellon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bny.codekatas.coffeeshopkata;

import bny.codekatas.coffeeshopkata.beverage.*;
import bny.codekatas.coffeeshopkata.food.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoffeeShopOrder {
    private static final String FOOD_DESCRIPTION_PATTERN = "%s: %s $%s\n";
    private final String customerName;
    private final List<Item> orderItems;

    public CoffeeShopOrder(String customerName, List<Item> orderItems) {
        this.customerName = customerName;
        this.orderItems = orderItems;
    }

    /**
     * Generate a receipt for a customer's food items.
     * If the item is a Donut: Print Donut: [donutType] $Price
     * If the item is a Cookie: Print Cookie: [cookieType] $Price
     * If the item is a Bagel: Print Bagel: [bagelType] $Price
     * Total: $Total Price
     * <p>
     * NOTE: The method highlights the usage of a record deconstruction pattern
     *
     * @see <a href="https://openjdk.org/jeps/440">...</a>
     */
    public String generateReceiptForFoodItems() {
        // TODO: Implement the receipt generation logic here.
        // Hint: look at the Java 8 implementation in the jdk8 module,
        // and the link above to see how record patterns can be utilized here
        double total = 0.0;
        StringBuilder items = new StringBuilder();

        for (Item item : orderItems) {
            if (item instanceof BakeryItem bakeryItem) {
                items.append(getFoodDescription(bakeryItem));
                total += bakeryItem.getPrice();
            }
        }

        return "%sTotal: $%s".formatted(items, total);
    }

    private static String getFoodDescription(BakeryItem bakeryItem) {
        return switch (bakeryItem) {
            case Donut(var type) ->
                    FOOD_DESCRIPTION_PATTERN.formatted("Donut", type, bakeryItem.getPrice());
            case Bagel(var bagelType, var spreadType, var toasted) ->
                    FOOD_DESCRIPTION_PATTERN.formatted("Bagel", bagelType, bakeryItem.getPrice());
            case Cookie(var cookieType, var warmed)  ->
                    FOOD_DESCRIPTION_PATTERN.formatted("Cookie", cookieType, bakeryItem.getPrice());
        };
    }

    /**
     * Return a list of custom strings for the customer's food items!
     * The string format for each food item is as follows:
     * If the item is a Bagel: "[bagelType] with [spreadType]"
     * If the item is a Cookie: "[cookieType] cookie"
     * If the item is a Donut: "[donutType] donut"
     * Otherwise: it is a beverage and should not be added to the list!
     * <p>
     * NOTE: This method show-cases a switch-case pattern matching.
     *
     * @see <a href="https://openjdk.org/jeps/441">...</a>
     */
    public List<String> getFoodItemsForOrder() {
        List<String> foodItems = new ArrayList<>();
        for (Item item : this.orderItems) {
            if (item instanceof BakeryItem bakeryItem) {
                foodItems.add(switch (bakeryItem) {
                    case Bagel(var bagelType, var spreadType, var toasted) -> bagelType + " bagel with " + spreadType;
                    case Cookie(var cookieType, var warmed) -> cookieType + " cookie";
                    case Donut donut -> donut.donutType() + " donut";

                });
            }
        }
        return foodItems;
    }

    /**
     * Return a list of custom strings for the customer's drinks!
     * First drink: Hot Americano
     * Second drink: Hot Caramel Latte with Almond Milk
     * Third drink: Hot Vanilla Macchiato with Whole Milk
     * Fourth drink: Matcha Tea
     * <p>
     * NOTE: This method utilize sealed classes and permit to define coffee drink types
     * (e.g., Americano, Latte, Macchiato) are allowed within a hierarchy.
     * However, Tea is not part of this hierarchy.
     */
    public List<String> getDrinksForOrder()
    {
        CoffeeDrink coffeeDrink1 = new Americano(DrinkTemperature.HOT);
        CoffeeDrink coffeeDrink2 = new Latte(FlavorSyrup.CARAMEL, MilkType.ALMOND_MILK, false, DrinkTemperature.HOT);
        CoffeeDrink coffeeDrink3 = new Macchiato(MilkType.WHOLE_MILK, FlavorSyrup.VANILLA, DrinkTemperature.HOT);
        Beverage tea = new Tea(TeaType.MATCHA);


        return new ArrayList<>(List.of(coffeeDrink1.toString(), coffeeDrink2.toString(), coffeeDrink3.toString(),tea.toString()));
    }
}
