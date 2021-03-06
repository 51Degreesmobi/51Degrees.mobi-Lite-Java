/* *********************************************************************
 * This Source Code Form is copyright of 51Degrees Mobile Experts Limited. 
 * Copyright © 2017 51Degrees Mobile Experts Limited, 5 Charlotte Close,
 * Caversham, Reading, Berkshire, United Kingdom RG4 7BY
 * 
 * This Source Code Form is the subject of the following patents and patent
 * applications, owned by 51Degrees Mobile Experts Limited of 5 Charlotte
 * Close, Caversham, Reading, Berkshire, United Kingdom RG4 7BY: 
 * European Patent No. 2871816;
 * European Patent Application No. 17184134.9;
 * United States Patent Nos. 9,332,086 and 9,350,823; and
 * United States Patent Application No. 15/686,066.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0.
 * 
 * If a copy of the MPL was not distributed with this file, You can obtain
 * one at http://mozilla.org/MPL/2.0/.
 * 
 * This Source Code Form is "Incompatible With Secondary Licenses", as
 * defined by the Mozilla Public License, v. 2.0.
 * ********************************************************************* */
package fiftyone.mobile.detection.entities;

import fiftyone.mobile.detection.Dataset;
import fiftyone.mobile.detection.readers.BinaryReader;
import java.io.IOException;

/**
 * Class used to link a property to one or more export maps.
 * <p>
 * Objects of this class should not be created directly as they are part of the 
 * internal logic. Use the relevant {@link Dataset} method to access these 
 * objects.
 * <p>
 * For more information see: 
 * <a href="https://51degrees.com/support/documentation/device-detection-data-model">
 * 51Degrees pattern data model</a>.
 */
public class Map extends BaseEntity implements Comparable<Map> {

    /**
     * The name of the map.
     *
     * @return name of the map.
     * @throws java.io.IOException indicates an I/O exception occurred
     */
    @SuppressWarnings("DoubleCheckedLocking")
    public String getName() throws IOException {
        String localName = name;
        if (localName == null) {
            synchronized (this) {
                localName = name;
                if (localName == null) {
                    name = localName = 
                            getDataSet().strings.get(nameIndex).toString();
                }
            }
        }
        return localName;
    }
    private volatile String name;
    private final int nameIndex;

    /**
     * Constructs a new instance of NodeIndex
     *
     * @param dataSet The data set the node is contained within
     * @param index The index of this object in the Node
     * @param reader BinaryReader object to be used
     */
    public Map(Dataset dataSet, int index, BinaryReader reader) {
        super(dataSet, index);
        this.nameIndex = reader.readInt32();
    }

    /**
     * Called after the entire data set has been loaded to ensure any further
     * initialisation steps that require other items in the data set can be
     * completed.
     * <p>
     * This method should not be called as it is part of the internal logic.
     */
    void init() throws IOException {
        getName();
    }

    /**
     * Compares this node index to another.
     *
     * @param other The node index to compare
     * @return Indication of relative value based on ComponentId field
     */
    @Override
    public int compareTo(Map other) {
        if (getDataSet() == other.getDataSet()) {
            return getIndex() - other.getIndex();
        }
        try {
            return getName().compareTo(other.getName());
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Converts the node index into a string.
     *
     * @return a string representation of the node characters
     */
    @Override
    public String toString() {
        try {
            return getName();
        } catch (IOException e) {
            return super.toString();
        }
    }
}
