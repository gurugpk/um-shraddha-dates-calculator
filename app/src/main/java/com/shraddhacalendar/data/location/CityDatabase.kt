package com.shraddhacalendar.data.location

import com.shraddhacalendar.core.models.GeoLocation

/**
 * Built-in pre-packaged offline database of 100+ prominent Indian cities,
 * holy pilgrimage centers (Mantralayam, Udupi, Kashi, Gaya, etc.), and international NRI hubs.
 */
object CityDatabase {

    val CITIES: List<GeoLocation> = listOf(
        // Karnataka
        GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata"),
        GeoLocation("Mysuru", "Karnataka", "India", 12.2958, 76.6394, "Asia/Kolkata"),
        GeoLocation("Hubballi", "Karnataka", "India", 15.3647, 75.1240, "Asia/Kolkata"),
        GeoLocation("Dharwad", "Karnataka", "India", 15.4589, 75.0078, "Asia/Kolkata"),
        GeoLocation("Belagavi", "Karnataka", "India", 15.8497, 74.4977, "Asia/Kolkata"),
        GeoLocation("Mangaluru", "Karnataka", "India", 12.9141, 74.8560, "Asia/Kolkata"),
        GeoLocation("Udupi", "Karnataka", "India", 13.3409, 74.7421, "Asia/Kolkata"),
        GeoLocation("Kalaburagi (Gulbarga)", "Karnataka", "India", 17.3297, 76.8343, "Asia/Kolkata"),
        GeoLocation("Ballari", "Karnataka", "India", 15.1394, 76.9214, "Asia/Kolkata"),
        GeoLocation("Davanagere", "Karnataka", "India", 14.4644, 75.9218, "Asia/Kolkata"),
        GeoLocation("Shivamogga", "Karnataka", "India", 13.9299, 75.5681, "Asia/Kolkata"),
        GeoLocation("Tumakuru", "Karnataka", "India", 13.3379, 77.1173, "Asia/Kolkata"),
        GeoLocation("Raichur", "Karnataka", "India", 16.2076, 77.3463, "Asia/Kolkata"),
        GeoLocation("Bidar", "Karnataka", "India", 17.9104, 77.5199, "Asia/Kolkata"),
        GeoLocation("Vijayapura (Bijapur)", "Karnataka", "India", 16.8302, 75.7100, "Asia/Kolkata"),
        GeoLocation("Hospet", "Karnataka", "India", 15.2689, 76.3909, "Asia/Kolkata"),
        GeoLocation("Gadag", "Karnataka", "India", 15.4167, 75.6167, "Asia/Kolkata"),
        GeoLocation("Bagalkot", "Karnataka", "India", 16.1691, 75.6615, "Asia/Kolkata"),
        GeoLocation("Chitradurga", "Karnataka", "India", 14.2251, 76.3980, "Asia/Kolkata"),
        GeoLocation("Hassan", "Karnataka", "India", 13.0033, 76.1004, "Asia/Kolkata"),
        GeoLocation("Sirsi", "Karnataka", "India", 14.6195, 74.8354, "Asia/Kolkata"),
        GeoLocation("Gokarna", "Karnataka", "India", 14.5479, 74.3188, "Asia/Kolkata"),
        GeoLocation("Sringeri", "Karnataka", "India", 13.4194, 75.2570, "Asia/Kolkata"),
        GeoLocation("Kukke Subramanya", "Karnataka", "India", 12.6644, 75.6144, "Asia/Kolkata"),
        GeoLocation("Dharmasthala", "Karnataka", "India", 12.9555, 75.3789, "Asia/Kolkata"),
        GeoLocation("Mulbagal", "Karnataka", "India", 13.1633, 78.3967, "Asia/Kolkata"),
        GeoLocation("Nanjangud", "Karnataka", "India", 12.1194, 76.6800, "Asia/Kolkata"),
        GeoLocation("Pajaka (Katapadi)", "Karnataka", "India", 13.2847, 74.7797, "Asia/Kolkata"),
        GeoLocation("Malkhed", "Karnataka", "India", 17.1897, 77.1683, "Asia/Kolkata"),

        // Sacred Pilgrimage Centers
        GeoLocation("Mantralayam", "Andhra Pradesh", "India", 15.9389, 77.4267, "Asia/Kolkata"),
        GeoLocation("Tirupati", "Andhra Pradesh", "India", 13.6288, 79.4192, "Asia/Kolkata"),
        GeoLocation("Pandharpur", "Maharashtra", "India", 17.6775, 75.3267, "Asia/Kolkata"),
        GeoLocation("Varanasi (Kashi)", "Uttar Pradesh", "India", 25.3176, 82.9739, "Asia/Kolkata"),
        GeoLocation("Gaya", "Bihar", "India", 24.7914, 85.0002, "Asia/Kolkata"),
        GeoLocation("Prayagraj (Allahabad)", "Uttar Pradesh", "India", 25.4358, 81.8463, "Asia/Kolkata"),
        GeoLocation("Ayodhya", "Uttar Pradesh", "India", 26.7922, 82.1998, "Asia/Kolkata"),
        GeoLocation("Puri", "Odisha", "India", 19.8135, 85.8312, "Asia/Kolkata"),
        GeoLocation("Rameswaram", "Tamil Nadu", "India", 9.2876, 79.3129, "Asia/Kolkata"),
        GeoLocation("Haridwar", "Uttarakhand", "India", 29.9457, 78.1642, "Asia/Kolkata"),
        GeoLocation("Rishikesh", "Uttarakhand", "India", 30.0869, 78.2676, "Asia/Kolkata"),
        GeoLocation("Mathura", "Uttar Pradesh", "India", 27.4924, 77.6737, "Asia/Kolkata"),
        GeoLocation("Dwarka", "Gujarat", "India", 22.2442, 68.9685, "Asia/Kolkata"),
        GeoLocation("Badrinath", "Uttarakhand", "India", 30.7433, 79.4938, "Asia/Kolkata"),
        GeoLocation("Kurukshetra", "Haryana", "India", 29.9695, 76.8783, "Asia/Kolkata"),

        // Maharashtra & Goa
        GeoLocation("Mumbai", "Maharashtra", "India", 19.0760, 72.8777, "Asia/Kolkata"),
        GeoLocation("Pune", "Maharashtra", "India", 18.5204, 73.8567, "Asia/Kolkata"),
        GeoLocation("Nagpur", "Maharashtra", "India", 21.1458, 79.0882, "Asia/Kolkata"),
        GeoLocation("Nashik", "Maharashtra", "India", 19.9975, 73.7898, "Asia/Kolkata"),
        GeoLocation("Kolhapur", "Maharashtra", "India", 16.7050, 74.2433, "Asia/Kolkata"),
        GeoLocation("Solapur", "Maharashtra", "India", 17.6599, 75.9064, "Asia/Kolkata"),
        GeoLocation("Aurangabad (Chhatrapati Sambhajinagar)", "Maharashtra", "India", 19.8762, 75.3433, "Asia/Kolkata"),
        GeoLocation("Panaji", "Goa", "India", 15.4909, 73.8278, "Asia/Kolkata"),

        // Andhra Pradesh & Telangana
        GeoLocation("Hyderabad", "Telangana", "India", 17.3850, 78.4867, "Asia/Kolkata"),
        GeoLocation("Secunderabad", "Telangana", "India", 17.4399, 78.4983, "Asia/Kolkata"),
        GeoLocation("Visakhapatnam", "Andhra Pradesh", "India", 17.6868, 83.2185, "Asia/Kolkata"),
        GeoLocation("Vijayawada", "Andhra Pradesh", "India", 16.5062, 80.6480, "Asia/Kolkata"),
        GeoLocation("Guntur", "Andhra Pradesh", "India", 16.3067, 80.4365, "Asia/Kolkata"),
        GeoLocation("Kurnool", "Andhra Pradesh", "India", 15.8281, 78.0373, "Asia/Kolkata"),
        GeoLocation("Anantapur", "Andhra Pradesh", "India", 14.6819, 77.6006, "Asia/Kolkata"),
        GeoLocation("Warangal", "Telangana", "India", 17.9689, 79.5941, "Asia/Kolkata"),

        // Tamil Nadu & Kerala
        GeoLocation("Chennai", "Tamil Nadu", "India", 13.0827, 80.2707, "Asia/Kolkata"),
        GeoLocation("Coimbatore", "Tamil Nadu", "India", 11.0168, 76.9558, "Asia/Kolkata"),
        GeoLocation("Madurai", "Tamil Nadu", "India", 9.9252, 78.1198, "Asia/Kolkata"),
        GeoLocation("Tiruchirappalli (Trichy)", "Tamil Nadu", "India", 10.7905, 78.7047, "Asia/Kolkata"),
        GeoLocation("Salem", "Tamil Nadu", "India", 11.6643, 78.1460, "Asia/Kolkata"),
        GeoLocation("Thanjavur", "Tamil Nadu", "India", 10.7870, 79.1378, "Asia/Kolkata"),
        GeoLocation("Kochi", "Kerala", "India", 9.9312, 76.2673, "Asia/Kolkata"),
        GeoLocation("Thiruvananthapuram", "Kerala", "India", 8.5241, 76.9366, "Asia/Kolkata"),
        GeoLocation("Kozhikode", "Kerala", "India", 11.2588, 75.7804, "Asia/Kolkata"),
        GeoLocation("Palakkad", "Kerala", "India", 10.7867, 76.6548, "Asia/Kolkata"),

        // North, Central & East India
        GeoLocation("New Delhi", "Delhi", "India", 28.6139, 77.2090, "Asia/Kolkata"),
        GeoLocation("Noida", "Uttar Pradesh", "India", 28.5355, 77.3910, "Asia/Kolkata"),
        GeoLocation("Gurugram", "Haryana", "India", 28.4595, 77.0266, "Asia/Kolkata"),
        GeoLocation("Kolkata", "West Bengal", "India", 22.5726, 88.3639, "Asia/Kolkata"),
        GeoLocation("Ahmedabad", "Gujarat", "India", 23.0225, 72.5714, "Asia/Kolkata"),
        GeoLocation("Surat", "Gujarat", "India", 21.1702, 72.8311, "Asia/Kolkata"),
        GeoLocation("Vadodara", "Gujarat", "India", 22.3072, 73.1812, "Asia/Kolkata"),
        GeoLocation("Jaipur", "Rajasthan", "India", 26.9124, 75.7873, "Asia/Kolkata"),
        GeoLocation("Lucknow", "Uttar Pradesh", "India", 26.8467, 80.9462, "Asia/Kolkata"),
        GeoLocation("Kanpur", "Uttar Pradesh", "India", 26.4499, 80.3319, "Asia/Kolkata"),
        GeoLocation("Patna", "Bihar", "India", 25.5941, 85.1376, "Asia/Kolkata"),
        GeoLocation("Chandigarh", "Chandigarh", "India", 30.7333, 76.7794, "Asia/Kolkata"),
        GeoLocation("Indore", "Madhya Pradesh", "India", 22.7196, 75.8577, "Asia/Kolkata"),
        GeoLocation("Bhopal", "Madhya Pradesh", "India", 23.2599, 77.4126, "Asia/Kolkata"),
        GeoLocation("Bhubaneswar", "Odisha", "India", 20.2961, 85.8245, "Asia/Kolkata"),
        GeoLocation("Guwahati", "Assam", "India", 26.1445, 91.7362, "Asia/Kolkata"),

        // International NRI Centers
        GeoLocation("San Jose", "California", "United States", 37.3382, -121.8863, "America/Los_Angeles"),
        GeoLocation("San Francisco", "California", "United States", 37.7749, -122.4194, "America/Los_Angeles"),
        GeoLocation("New York", "New York", "United States", 40.7128, -74.0060, "America/New_York"),
        GeoLocation("Jersey City", "New Jersey", "United States", 40.7178, -74.0431, "America/New_York"),
        GeoLocation("Chicago", "Illinois", "United States", 41.8781, -87.6298, "America/Chicago"),
        GeoLocation("Dallas", "Texas", "United States", 32.7767, -96.7970, "America/Chicago"),
        GeoLocation("Houston", "Texas", "United States", 29.7604, -95.3698, "America/Chicago"),
        GeoLocation("Austin", "Texas", "United States", 30.2672, -97.7431, "America/Chicago"),
        GeoLocation("Seattle", "Washington", "United States", 47.6062, -122.3321, "America/Los_Angeles"),
        GeoLocation("Atlanta", "Georgia", "United States", 33.7490, -84.3880, "America/New_York"),
        GeoLocation("Boston", "Massachusetts", "United States", 42.3601, -71.0589, "America/New_York"),
        GeoLocation("Toronto", "Ontario", "Canada", 43.6532, -79.3832, "America/Toronto"),
        GeoLocation("Vancouver", "British Columbia", "Canada", 49.2827, -123.1207, "America/Vancouver"),
        GeoLocation("London", "Greater London", "United Kingdom", 51.5074, -0.1278, "Europe/London"),
        GeoLocation("Manchester", "Greater Manchester", "United Kingdom", 53.4808, -2.2426, "Europe/London"),
        GeoLocation("Edinburgh", "Scotland", "United Kingdom", 55.9533, -3.1883, "Europe/London"),
        GeoLocation("Dublin", "Leinster", "Ireland", 53.3498, -6.2603, "Europe/Dublin"),
        GeoLocation("Dubai", "Dubai", "United Arab Emirates", 25.2048, 55.2708, "Asia/Dubai"),
        GeoLocation("Abu Dhabi", "Abu Dhabi", "United Arab Emirates", 24.4539, 54.3773, "Asia/Dubai"),
        GeoLocation("Doha", "Doha", "Qatar", 25.2854, 51.5310, "Asia/Qatar"),
        GeoLocation("Muscat", "Muscat", "Oman", 23.5880, 58.3829, "Asia/Muscat"),
        GeoLocation("Singapore", "Central", "Singapore", 1.3521, 103.8198, "Asia/Singapore"),
        GeoLocation("Kuala Lumpur", "Federal Territory", "Malaysia", 3.1390, 101.6869, "Asia/Kuala_Lumpur"),
        GeoLocation("Sydney", "New South Wales", "Australia", -33.8688, 151.2093, "Australia/Sydney"),
        GeoLocation("Melbourne", "Victoria", "Australia", -37.8136, 144.9631, "Australia/Melbourne"),
        GeoLocation("Brisbane", "Queensland", "Australia", -27.4698, 153.0251, "Australia/Brisbane"),
        GeoLocation("Perth", "Western Australia", "Australia", -31.9505, 115.8605, "Australia/Perth"),
        GeoLocation("Auckland", "Auckland", "New Zealand", -36.8485, 174.7633, "Pacific/Auckland"),
        GeoLocation("Frankfurt", "Hesse", "Germany", 50.1109, 8.6821, "Europe/Berlin"),
        GeoLocation("Berlin", "Berlin", "Germany", 52.5200, 13.4050, "Europe/Berlin"),
        GeoLocation("Munich", "Bavaria", "Germany", 48.1351, 11.5820, "Europe/Berlin"),
        GeoLocation("Paris", "Île-de-France", "France", 48.8566, 2.3522, "Europe/Paris"),
        GeoLocation("Amsterdam", "North Holland", "Netherlands", 52.3676, 4.9041, "Europe/Amsterdam"),
        GeoLocation("Zurich", "Zurich", "Switzerland", 47.3769, 8.5417, "Europe/Zurich"),
        GeoLocation("Tokyo", "Kanto", "Japan", 35.6762, 139.6503, "Asia/Tokyo")
    )

    fun search(query: String): List<GeoLocation> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return CITIES.take(20)
        return CITIES.filter {
            it.city.lowercase().contains(q) ||
                    it.state.lowercase().contains(q) ||
                    it.country.lowercase().contains(q)
        }
    }
}
