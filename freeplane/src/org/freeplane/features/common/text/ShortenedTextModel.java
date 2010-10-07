/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.common.text;

import org.freeplane.core.extension.IExtension;
import org.freeplane.features.common.map.NodeModel;

/**
 * @author Dimitry Polivaev
 */
public class ShortenedTextModel implements IExtension {
	public enum State{HIDDEN, SHOWN, DISABLED};
	private State state = State.SHOWN;
	public ShortenedTextModel(State shortened) {
	    this.state = shortened;
    }

	public State getState() {
    	return state;
    }

	public void setState(State shortened) {
    	this.state = shortened;
    }

	public static ShortenedTextModel createShortenedTextModel(final NodeModel node) {
		ShortenedTextModel model = ShortenedTextModel.getShortenedTextModel(node);
		if (model == null) {
			model = new ShortenedTextModel(State.DISABLED);
			node.addExtension(model);
		}
		return model;
	}

	public static ShortenedTextModel getShortenedTextModel(final NodeModel node) {
		final ShortenedTextModel extension = (ShortenedTextModel) node.getExtension(ShortenedTextModel.class);
		return extension;
	}
	
	public static State getState(final NodeModel node){
		 final ShortenedTextModel shortened = getShortenedTextModel(node);
		 return shortened != null ? shortened.getState() : State.DISABLED;
	}
}
