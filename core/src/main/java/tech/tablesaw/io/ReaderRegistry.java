package tech.tablesaw.io;

import java.util.HashMap;
import java.util.Map;

public class ReaderRegistry {

    private final Map<String, TablesawReader<?>> optionTypesRegistry = new HashMap<>();
    private final Map<String, TablesawMultiReader<?>> optionTypesMultiRegistry = new HashMap<>();

    private final Map<String, TablesawReader<?>> extensionsRegistry = new HashMap<>();
    private final Map<String, TablesawMultiReader<?>> extensionsMultiRegistry = new HashMap<>();

    private final Map<String, TablesawReader<?>> mimeTypesRegistry = new HashMap<>();
    private final Map<String, TablesawMultiReader<?>> mimeTypesMultiRegistry = new HashMap<>();

    
    public void registerOptions(Class<? extends ReadOptions> optionsType, TablesawReader<?> reader) {
	optionTypesRegistry.put(optionsType.getCanonicalName(), reader);
    }
    public void registerOptions(Class<? extends ReadOptions> optionsType, TablesawMultiReader<?> reader) {
	optionTypesMultiRegistry.put(optionsType.getCanonicalName(), reader);
    }

    public void registerExtension(String extension, TablesawReader<?> reader) {
	extensionsRegistry.put(extension, reader);
    }
    public void registerExtension(String extension, TablesawMultiReader<?> reader) {
	extensionsMultiRegistry.put(extension, reader);
    }

    public void registerMimeType(String mimeType, TablesawReader<?> reader) {
	mimeTypesRegistry.put(mimeType, reader);
    }
    public void registerMimeType(String mimeType, TablesawMultiReader<?> reader) {
	mimeTypesMultiRegistry.put(mimeType, reader);
    }

    public TablesawReader<?> getReaderForOptions(ReadOptions options) {
	return optionTypesRegistry.get(options.getClass().getCanonicalName());
    }
    public TablesawMultiReader<?> getMultiReaderForOptionsType(ReadOptions options) {
	return optionTypesMultiRegistry.get(options.getClass().getCanonicalName());
    }

    public TablesawReader<?> getReaderForExtension(String extension) {
	return extensionsRegistry.get(extension);
    }
    public TablesawMultiReader<?> getMultiReaderForExtension(String extension) {
	return extensionsMultiRegistry.get(extension);
    }

    public TablesawReader<?> getReaderForMimeType(String mimeType) {
	return mimeTypesRegistry.get(mimeType);
    }
    public TablesawMultiReader<?> getMultiReaderForMimeType(String mimeType) {
	return mimeTypesMultiRegistry.get(mimeType);
    }

}
