package com.horizonpack.horizoncore.data;

import com.horizonpack.horizoncore.core.HorizonRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class TechnologyGraph {

    // adjacency: tech → list of techs that depend on it
    private static final Map<ResourceLocation, List<ResourceLocation>> dependents = new HashMap<>();
    // reverse: tech → its prerequisites
    private static final Map<ResourceLocation, List<ResourceLocation>> prerequisites = new HashMap<>();

    public static void buildGraph() {
        dependents.clear();
        prerequisites.clear();

        // Iterate every registered TechnologyData using 1.21.1 DeferredHolder syntax
        HorizonRegistries.TECHNOLOGIES.getEntries().forEach(entry -> {
            ResourceLocation id = entry.getId(); // Changed for 1.21.1
            List<ResourceLocation> prereqs = entry.get().getPrerequisites(); // Changed for 1.21.1

            prerequisites.put(id, prereqs);

            for (ResourceLocation prereq : prereqs) {
                dependents.computeIfAbsent(prereq, k -> new ArrayList<>()).add(id);
            }
        });

        validateNoCycles();
    }

    /** Returns all technologies that are immediately unlockable given a set of already-unlocked techs. */
    public static List<ResourceLocation> getAvailableTechs(Set<ResourceLocation> unlocked, HorizonAge currentAge) {
        List<ResourceLocation> available = new ArrayList<>();

        HorizonRegistries.TECHNOLOGIES.getEntries().forEach(entry -> {
            ResourceLocation id = entry.getId(); // Changed for 1.21.1
            TechnologyData tech = entry.get();   // Changed for 1.21.1

            if (unlocked.contains(id)) return;
            if (!currentAge.isAtLeast(tech.getRequiredAge())) return;
            if (unlocked.containsAll(tech.getPrerequisites())) available.add(id);
        });
        return available;
    }

    /** Returns all techs that this tech directly unlocks (i.e., techs that list it as a prerequisite). */
    public static List<ResourceLocation> getDependents(ResourceLocation techId) {
        return dependents.getOrDefault(techId, List.of());
    }

    private static void validateNoCycles() {
        Map<ResourceLocation, Integer> inDegree = new HashMap<>();
        Set<ResourceLocation> allNodes = new HashSet<>();

        // Initialize degrees based on registered technologies
        prerequisites.forEach((id, prereqs) -> {
            allNodes.add(id);
            inDegree.put(id, prereqs.size());
            for (ResourceLocation p : prereqs) allNodes.add(p);
        });

        Queue<ResourceLocation> queue = new LinkedList<>();
        inDegree.forEach((id, degree) -> {
            if (degree == 0) queue.add(id);
        });

        int visitedCount = 0;
        while (!queue.isEmpty()) {
            ResourceLocation current = queue.poll();
            visitedCount++;

            List<ResourceLocation> neighbors = dependents.getOrDefault(current, List.of());
            for (ResourceLocation neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) queue.add(neighbor);
            }
        }

        if (visitedCount < prerequisites.size()) {
            // Throw a warning or fatal error if the graph is broken
            net.minecraft.server.MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                System.err.println("FATAL: Circular dependency detected in Technology Graph! Only " + visitedCount + "/" + prerequisites.size() + " nodes are reachable.");
            }
        }
    }
}