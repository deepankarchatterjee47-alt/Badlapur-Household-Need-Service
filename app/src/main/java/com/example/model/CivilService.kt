package com.example.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class ServiceType(
    val title: String,
    val description: String,
    val iconName: String,
    val baseInspectionFee: Int
) {
    PLUMBING(
        title = "Plumbing",
        description = "Pipes, taps, water tanks, leakages, blockage clearing, sanitaryware",
        iconName = "plumbing",
        baseInspectionFee = 149
    ),
    FABRICATION(
        title = "Fabrication",
        description = "Safety grills, iron gates, sliding windows, welding, shed fabrication",
        iconName = "fabrication",
        baseInspectionFee = 199
    ),
    FURNITURE(
        title = "Furniture & Carpentry",
        description = "Custom woodwork, modular kitchen repair, bed/sofa assembly, door locks",
        iconName = "furniture",
        baseInspectionFee = 199
    ),
    PAINTING(
        title = "Painting",
        description = "Interior/exterior walls, waterproof coating, wall putty, texture finish",
        iconName = "painting",
        baseInspectionFee = 199
    ),
    TILES(
        title = "Tiles & Masonry",
        description = "Floor/wall tiles, bathroom renovation, plastering, granite counters",
        iconName = "tiles",
        baseInspectionFee = 199
    ),
    ELECTRICIAN(
        title = "Electrician",
        description = "Wiring, switchboard repair, MCB fix, geyser & inverter installation, fans",
        iconName = "electrician",
        baseInspectionFee = 149
    ),
    HOME_MAID(
        title = "Home Maid Service",
        description = "Deep house cleaning, kitchen scrubbing, bathroom sanitation, dusting & mopping",
        iconName = "maid",
        baseInspectionFee = 199
    )
}

data class ServiceTaskItem(
    val id: String,
    val serviceType: ServiceType,
    val name: String,
    val description: String,
    val typicalLaborRange: String,
    val commonMaterialsNeeded: String
)

val CIVIL_TASKS = listOf(
    // Plumbing
    ServiceTaskItem("plumb_1", ServiceType.PLUMBING, "Tap & Mixer Leakage Repair", "Fix dripping taps, mixer cartridge, washer changes", "₹150 - ₹300", "Washer, Teflon tape, CPVC tap spindle"),
    ServiceTaskItem("plumb_2", ServiceType.PLUMBING, "Pipe Fitting & Concealed Leakage", "Diagnose and fix wall leakages, pipe joint repairs", "₹400 - ₹900", "CPVC pipes, elbows, solvent cement"),
    ServiceTaskItem("plumb_3", ServiceType.PLUMBING, "Toilet & Flush Tank Servicing", "Flush siphon, dual push button, water closet fitting", "₹250 - ₹500", "Flush ball valve, internal siphon kit"),
    ServiceTaskItem("plumb_4", ServiceType.PLUMBING, "Water Tank & Motor Pump Setup", "Overhead tank connection, float valve, water motor install", "₹500 - ₹1200", "Float valve, brass union, PVC connector"),

    // Fabrication
    ServiceTaskItem("fab_1", ServiceType.FABRICATION, "Window Safety Grill & Railing", "Measurement, cutting, welding, primer & rust proofing", "₹600 - ₹1800", "MS square pipe, welding rods, red oxide"),
    ServiceTaskItem("fab_2", ServiceType.FABRICATION, "Main Iron Gate & Latch Repair", "Hinges realignment, heavy duty lock welding, latch", "₹450 - ₹1200", "Heavy hinge, MS flat bar, welding electrode"),
    ServiceTaskItem("fab_3", ServiceType.FABRICATION, "Balcony Shed & Polycarbonate Roof", "GI frame structure, corrugated sheet or polycarbonate", "₹1200 - ₹3500", "GI pipes, roof sheets, self-tapping screws"),

    // Furniture & Carpentry
    ServiceTaskItem("furn_1", ServiceType.FURNITURE, "Door Lock, Handles & Hinge Repair", "Mortise lock, magnetic catchers, hydraulic closer repair", "₹200 - ₹450", "Godrej lock cylinder, brass screws, hinges"),
    ServiceTaskItem("furn_2", ServiceType.FURNITURE, "Wardrobe & Modular Kitchen Shutter Fix", "Drawer channels, soft-close hinges, plywood alignment", "₹350 - ₹800", "Telescopic channels, auto hinges, fevicol"),
    ServiceTaskItem("furn_3", ServiceType.FURNITURE, "Custom Furniture & Wood Polish", "Table/bed repairs, touch-up polish or PU coating", "₹500 - ₹1500", "Touchwood polish, sanding paper, thinner"),

    // Painting
    ServiceTaskItem("paint_1", ServiceType.PAINTING, "Interior Room Wall Touch-up & Painting", "Wall scraper, 2 coats primer + tractor/royale emulsion", "₹800 - ₹2500", "Wall putty, emulsion paint, masking tape"),
    ServiceTaskItem("paint_2", ServiceType.PAINTING, "Dampness & Waterproof Coating", "Ceiling/balcony damp patch treatment with waterproof polymer", "₹600 - ₹1800", "Dr. Fixit waterproofing compound, brush"),
    ServiceTaskItem("paint_3", ServiceType.PAINTING, "Exterior Wall & Grill Enamel Paint", "Rust scraping and synthetic enamel gloss painting", "₹700 - ₹2000", "Enamel paint, red oxide primer, thinner"),

    // Tiles & Masonry
    ServiceTaskItem("tiles_1", ServiceType.TILES, "Floor & Wall Tile Replacement / Fitting", "Tile cutting, leveling, adhesive cement laying, grouting", "₹500 - ₹1600", "Tile adhesive/white cement, epoxy grout"),
    ServiceTaskItem("tiles_2", ServiceType.TILES, "Bathroom Renovation & Slope Correction", "Re-sloping floor to drain, wall skirting, water barrier", "₹1200 - ₹3000", "Waterproof mortar, floor tiles, drain jali"),
    ServiceTaskItem("tiles_3", ServiceType.TILES, "Granite Kitchen Counter / Basin Top", "Polishing edges, sink hole cutting, silicon sealing", "₹800 - ₹2200", "Granite adhesive, silicone sealant, diamond disc"),

    // Electrician
    ServiceTaskItem("elec_1", ServiceType.ELECTRICIAN, "Switchboard & Socket Replacement", "Modular switches, wiring check, power plug socket fix", "₹150 - ₹350", "Modular switch/socket, electrical insulation tape"),
    ServiceTaskItem("elec_2", ServiceType.ELECTRICIAN, "Ceiling Fan, Exhaust & Light Fitting", "Fan assembly, downrod installation, LED profile/panel light", "₹200 - ₹450", "Anchor fasteners, wiring, light clips"),
    ServiceTaskItem("elec_3", ServiceType.ELECTRICIAN, "MCB Tripping & Short Circuit Fix", "Load balancing, fuse box rewire, earth leakage inspection", "₹350 - ₹800", "MCB switch, copper wire 2.5/4 sq mm"),
    ServiceTaskItem("elec_4", ServiceType.ELECTRICIAN, "Geyser & Inverter Wiring Installation", "Heavy line power point, earth connection, copper pipe set", "₹400 - ₹950", "16A socket box, heavy duty cable, clips"),

    // Home Maid
    ServiceTaskItem("maid_1", ServiceType.HOME_MAID, "Complete Deep House Cleaning", "Intensive room-by-room dusting, floor scrubbing, windows", "₹800 - ₹1800", "Disinfectant liquid, floor cleaner, microfiber cloths"),
    ServiceTaskItem("maid_2", ServiceType.HOME_MAID, "Kitchen Deep Cleaning & Degreasing", "Exhaust fan, tiles oil-grease removal, cabinet wipe-down", "₹500 - ₹1100", "Degreaser spray, scrub pads, tile cleaner"),
    ServiceTaskItem("maid_3", ServiceType.HOME_MAID, "Bathroom Deep Sanitation & Descaling", "Hard water stain removal, toilet pot sanitation, mirror clean", "₹350 - ₹750", "Acid-free descaler, toilet cleaner, brush")
)
