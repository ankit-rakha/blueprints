package com.tinkerpop.blueprints.impls.orient;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Query;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.DefaultQuery;
import com.tinkerpop.blueprints.util.MultiIterable;
import com.tinkerpop.blueprints.util.StringFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Luca Garulli (http://www.orientechnologies.com)
 */
public class OrientVertex extends OrientElement implements Vertex {

    public OrientVertex() {
        super(null, new ODocument());
    }

    public OrientVertex(final OrientGraph rawGraph, final ODocument rawVertex) {
        super(rawGraph, rawVertex);
    }

    public Iterable<Edge> getOutEdges(final String... labels) {
        if (this.rawElement == null)
            return Collections.emptyList();

        if (labels.length == 0) {
            Set<OIdentifiable> edges = graph.getRawGraph().getOutEdges(this.rawElement, null);
            if (!edges.isEmpty())
                // WRAP IT TO VOID CONCURRENT MODIFICATION EXCEPTIONS
                edges = new HashSet<OIdentifiable>(edges);
            return new OrientElementIterable<Edge>(graph, edges);
        } else if (labels.length == 1) {
            return new OrientElementIterable<Edge>(graph, graph.getRawGraph().getOutEdges(this.rawElement, labels[0]));
        } else {
            final List<Iterable<Edge>> edges = new ArrayList<Iterable<Edge>>();
            for (final String label : labels) {
                edges.add(new OrientElementIterable<Edge>(graph, graph.getRawGraph().getOutEdges(this.rawElement, label)));
            }
            return new MultiIterable<Edge>(edges);
        }
    }

    public Iterable<Edge> getInEdges(final String... labels) {
        if (this.rawElement == null)
            return Collections.emptyList();

        if (labels.length == 0) {
            Set<OIdentifiable> edges = graph.getRawGraph().getInEdges(this.rawElement, null);
            if (!edges.isEmpty())
                // WRAP IT TO VOID CONCURRENT MODIFICATION EXCEPTIONS
                edges = new HashSet<OIdentifiable>(edges);
            return new OrientElementIterable<Edge>(graph, edges);
        } else if (labels.length == 1) {
            return new OrientElementIterable<Edge>(graph, graph.getRawGraph().getInEdges(this.rawElement, labels[0]));
        } else {
            final List<Iterable<Edge>> edges = new ArrayList<Iterable<Edge>>();
            for (final String label : labels) {
                edges.add(new OrientElementIterable<Edge>(graph, graph.getRawGraph().getInEdges(this.rawElement, label)));
            }
            return new MultiIterable<Edge>(edges);
        }
    }

    public Query query() {
        return new DefaultQuery(this);
    }


    public Set<String> getPropertyKeys() {
        final Set<String> set = super.getPropertyKeys();
        if (set.size() > 0) {
            set.remove(OGraphDatabase.VERTEX_FIELD_IN);
            set.remove(OGraphDatabase.VERTEX_FIELD_OUT);
        }
        return set;
    }

    public String toString() {
        return StringFactory.vertexString(this);
    }
}