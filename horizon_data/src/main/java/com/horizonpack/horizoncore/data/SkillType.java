package com.horizonpack.horizoncore.data;

public enum SkillType {
    FORAGING        ("Foraging",        "survival"),
    HUNTING         ("Hunting",         "survival"),
    FISHING         ("Fishing",         "survival"),
    COOKING         ("Cooking",         "survival"),
    MEDICINE        ("Medicine",        "survival"),
    NAVIGATION      ("Navigation",      "survival"),
    MELEE           ("Melee Combat",    "combat"),
    ARCHERY         ("Archery",         "combat"),
    SHIELD_USE      ("Shield Use",      "combat"),
    TACTICS         ("Tactics",         "combat"),
    METALLURGY      ("Metallurgy",      "crafting"),
    CARPENTRY       ("Carpentry",       "crafting"),
    MASONRY         ("Masonry",         "crafting"),
    LEATHERWORKING  ("Leatherworking",  "crafting"),
    TEXTILES        ("Textiles",        "crafting"),
    POTTERY         ("Pottery",         "crafting"),
    ALCHEMY         ("Alchemy",         "crafting"),
    ENGINEERING     ("Engineering",     "crafting"),
    FARMING         ("Farming",         "agriculture"),
    ANIMAL_HUSBANDRY("Animal Husbandry","agriculture"),
    BOTANY          ("Botany",          "agriculture"),
    LITERACY        ("Literacy",        "academic"),
    MATHEMATICS     ("Mathematics",     "academic"),
    PHILOSOPHY      ("Philosophy",      "academic"),
    SCIENCE         ("Science",         "academic"),
    HISTORY         ("History",         "academic"),
    DIPLOMACY       ("Diplomacy",       "social"),
    TRADING         ("Trading",         "social"),
    LEADERSHIP      ("Leadership",      "social"),
    ESPIONAGE       ("Espionage",       "social"),
    RHETORIC        ("Rhetoric",        "social"),
    MINING          ("Mining",          "exploration"),
    GEOLOGY         ("Geology",         "exploration"),
    CARTOGRAPHY     ("Cartography",     "exploration"),
    SAILING         ("Sailing",         "exploration");

    private final String displayName;
    private final String domain;

    SkillType(String displayName, String domain) {
        this.displayName = displayName;
        this.domain      = domain;
    }

    public String getDisplayName() { return displayName; }
    public String getDomain()      { return domain; }
}