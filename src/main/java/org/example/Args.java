package org.example;

import java.util.ArrayList;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = "-help", help = true, description = "Help for all arguments, their brief description and default values")
    public boolean help;

    @Parameter(names = "-o", description = "Path to place results")
    public String path = "";

    @Parameter(names = "-p", description = "Prefix to result files")
    public String prefix = "";

    @Parameter(names = "-a", description = "Append toggle, will append lines in existing files instead of rewriting")
    public boolean append = false;

    @Parameter(names = "-s", description = "Short statistics for each data type")
    public boolean short_stat = false;

    @Parameter(names = "-f", description = "Full statistics for each data type")
    public boolean full_stat = false;

    @Parameter(description = "List of files to read")
    public ArrayList<String> input_files_paths = new ArrayList<>();
}