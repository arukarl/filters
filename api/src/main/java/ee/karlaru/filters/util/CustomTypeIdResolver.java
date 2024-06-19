package ee.karlaru.filters.util;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import ee.karlaru.filters.domain.Criterion;
import ee.karlaru.filters.exception.InvalidCriterionException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CustomTypeIdResolver extends TypeIdResolverBase {

    private static final Map<String, Class<? extends Criterion>> TYPE_MAP = new HashMap<>();


    static {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo()
                .acceptPackages("ee.karlaru.filters.domain.criterion_impl").scan()) {

            ClassInfoList classInfos = scanResult.getSubclasses(Criterion.class.getName());
            for (Class<?> subType : classInfos.loadClasses()) {
                try {
                    String name = (String) subType.getField("NAME").get(null);
                    TYPE_MAP.put(name.toUpperCase(), (Class<? extends Criterion>) subType);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new InvalidCriterionException("Failed to access NAME field in " + subType.getName());
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public String idFromValue(Object value) {
        return (String) value.getClass().getField("NAME").get(null);
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        Class<? extends Criterion> subType = TYPE_MAP.get(id.toUpperCase());
        if (subType == null) {
            throw new InvalidCriterionException("Unknown criterion fieldType: " + id);
        }
        return context.constructType(subType);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
