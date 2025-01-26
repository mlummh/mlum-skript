package ch.njol.yggdrasil;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.Nullable;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.*;

public class JRESerializer extends YggdrasilSerializer<Object> {
	
	private static final List<Class<?>> SUPPORTED_CLASSES = ImmutableList.of(
			ArrayList.class, LinkedList.class, HashSet.class, HashMap.class, UUID.class);
	
	@Override
	@Nullable
	public Class<?> getClass(String id) {
		for (Class<?> type : SUPPORTED_CLASSES)
			if (type.getSimpleName().equals(id))
				return type;
		return null;
	}
	
	@Override
	@Nullable
	public String getID(Class<?> type) {
		if (SUPPORTED_CLASSES.contains(type))
			return type.getSimpleName();
		return null;
	}
	
	@Override
	public Fields serialize(Object object) {
		if (!SUPPORTED_CLASSES.contains(object.getClass()))
			throw new IllegalArgumentException();
		Fields fields = new Fields();
		if (object instanceof Collection) {
			Collection<?> collection = ((Collection<?>) object);
			fields.putObject("values", collection.toArray());
		} else if (object instanceof Map) {
			Map<?, ?> map = ((Map<?, ?>) object);
			fields.putObject("keys", map.keySet().toArray());
			fields.putObject("values", map.values().toArray());
		} else if (object instanceof UUID) {
			fields.putPrimitive("mostSigBits", ((UUID) object).getMostSignificantBits());
			fields.putPrimitive("leastSigBits", ((UUID) object).getLeastSignificantBits());
		}
		assert fields.size() > 0 : object;
		return fields;
	}
	
	@Override
	public boolean canBeInstantiated(Class<?> type) {
		return type != UUID.class;
	}
	
	@Override
	@Nullable
	public <T> T newInstance(Class<T> type) {
		try {
			//noinspection deprecation
			return type.newInstance();
		} catch (InstantiationException e) { // all collections handled here have public nullary constructors
			e.printStackTrace();
			assert false;
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			assert false;
			return null;
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void deserialize(Object object, Fields fields) throws StreamCorruptedException {
		try {
			if (object instanceof Collection) {
				Collection<?> collection = ((Collection<?>) object);
				Object[] values = fields.getObject("values", Object[].class);
				if (values == null)
					throw new StreamCorruptedException();
				collection.addAll((Collection) Arrays.asList(values));
				return;
			} else if (object instanceof Map) {
				Map<?, ?> map = ((Map<?, ?>) object);
				Object[] keys = fields.getObject("keys", Object[].class), values = fields.getObject("values", Object[].class);
				if (keys == null || values == null || keys.length != values.length)
					throw new StreamCorruptedException();
				for (int i = 0; i < keys.length; i++)
					((Map) map).put(keys[i], values[i]);
				return;
			}
		} catch (Exception e) {
			throw new StreamCorruptedException(e.getClass().getSimpleName() + ": " + e.getMessage());
		}
		throw new StreamCorruptedException();
	}
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public <E> E deserialize(Class<E> type, Fields fields) throws StreamCorruptedException, NotSerializableException {
		if (type == UUID.class)
			return (E) new UUID(
					fields.getPrimitive("mostSigBits", Long.TYPE),
					fields.getPrimitive("leastSigBits", Long.TYPE));
		
		throw new StreamCorruptedException();
	}
	
}
