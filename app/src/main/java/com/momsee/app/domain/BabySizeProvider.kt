package com.momsee.app.domain

data class BabySizeInfo(
    val week: Int,
    val comparison: String,
    val lengthCm: String,
    val weightG: String,
    val isCrownToHeel: Boolean = false, // false = Crown-to-Rump (CRL), true = Crown-to-Heel (CHL)
)

object BabySizeProvider {
    /**
     * Common medical/developmental baby size comparisons.
     * Sources: ACOG standards, Mayo Clinic, and Hadlock standards.
     * Note: Measurements shift from Crown-to-Rump (CRL) to Crown-to-Heel (CHL) at Week 14.
     */
    val sizes = listOf(
        BabySizeInfo(4, "Poppy Seed", "0.2 cm", "0.1 g"),
        BabySizeInfo(5, "Orange Seed", "0.3 cm", "0.2 g"),
        BabySizeInfo(6, "Sweet Pea", "0.5 cm", "0.3 g"),
        BabySizeInfo(7, "Blueberry", "1.2 cm", "0.5 g"),
        BabySizeInfo(8, "Raspberry", "1.6 cm", "1 g"),
        BabySizeInfo(9, "Green Olive", "2.3 cm", "2 g"),
        BabySizeInfo(10, "Prune", "3.1 cm", "4 g"),
        BabySizeInfo(11, "Brussels Sprout", "4.1 cm", "7 g"),
        BabySizeInfo(12, "Lime", "5.4 cm", "14 g"),
        BabySizeInfo(13, "Lemon", "7.4 cm", "23 g"),
        
        // Transition to Head-to-Toe (CHL) at Week 14
        BabySizeInfo(14, "Nectarine", "14.7 cm", "43 g", isCrownToHeel = true),
        BabySizeInfo(15, "Apple", "16.7 cm", "70 g", isCrownToHeel = true),
        BabySizeInfo(16, "Avocado", "18.6 cm", "100 g", isCrownToHeel = true),
        BabySizeInfo(17, "Pear", "20.4 cm", "140 g", isCrownToHeel = true),
        BabySizeInfo(18, "Sweet Potato", "22.2 cm", "190 g", isCrownToHeel = true),
        BabySizeInfo(19, "Mango", "24 cm", "240 g", isCrownToHeel = true),
        BabySizeInfo(20, "Banana", "25.7 cm", "300 g", isCrownToHeel = true),
        BabySizeInfo(21, "Carrot", "26.7 cm", "360 g", isCrownToHeel = true),
        BabySizeInfo(22, "Papaya", "27.8 cm", "430 g", isCrownToHeel = true),
        BabySizeInfo(23, "Grapefruit", "28.9 cm", "501 g", isCrownToHeel = true),
        BabySizeInfo(24, "Corn", "30 cm", "600 g", isCrownToHeel = true),
        BabySizeInfo(25, "Rutabaga", "34.6 cm", "660 g", isCrownToHeel = true),
        BabySizeInfo(26, "Scallion", "35.6 cm", "760 g", isCrownToHeel = true),
        BabySizeInfo(27, "Cauliflower", "36.6 cm", "875 g", isCrownToHeel = true),
        BabySizeInfo(28, "Eggplant", "37.6 cm", "1 kg", isCrownToHeel = true),
        BabySizeInfo(29, "Butternut Squash", "38.6 cm", "1.1 kg", isCrownToHeel = true),
        BabySizeInfo(30, "Cabbage", "39.9 cm", "1.3 kg", isCrownToHeel = true),
        BabySizeInfo(31, "Coconut", "41.1 cm", "1.5 kg", isCrownToHeel = true),
        BabySizeInfo(32, "Jicama", "42.4 cm", "1.7 kg", isCrownToHeel = true),
        BabySizeInfo(33, "Pineapple", "43.7 cm", "1.9 kg", isCrownToHeel = true),
        BabySizeInfo(34, "Cantaloupe", "45 cm", "2.1 kg", isCrownToHeel = true),
        BabySizeInfo(35, "Honeydew", "46.2 cm", "2.4 kg", isCrownToHeel = true),
        BabySizeInfo(36, "Lettuce", "47.4 cm", "2.6 kg", isCrownToHeel = true),
        BabySizeInfo(37, "Swiss Chard", "48.6 cm", "2.9 kg", isCrownToHeel = true),
        BabySizeInfo(38, "Leek", "49.8 cm", "3.1 kg", isCrownToHeel = true),
        BabySizeInfo(39, "Watermelon", "50.7 cm", "3.3 kg", isCrownToHeel = true),
        BabySizeInfo(40, "Pumpkin", "51.2 cm", "3.5 kg", isCrownToHeel = true)
    )

    fun getSizeForWeek(week: Int): BabySizeInfo? {
        return sizes.find { it.week == week } ?: sizes.lastOrNull { it.week <= week }
    }
}
