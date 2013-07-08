/*
 * The ermineJ project
 * 
 * Copyright (c) 2013 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ubic.erminej.data;

/**
 * This is the same as ArrayDesignValueObject from Gemma. We don't use a direct dependency because of incompatible java
 * versions, which made it more trouble that it was worth.
 * 
 * @author Paul
 * @version $Id$
 */
public class Platform implements java.io.Serializable, Comparable<Platform> {

    private static final long serialVersionUID = -8259245319391937522L;

    private String color;

    private String dateCached;

    private java.util.Date dateCreated;

    private String description;

    private Long designElementCount;

    private Long expressionExperimentCount;

    private Boolean hasBlatAssociations;

    private Boolean hasGeneAssociations;

    private Boolean hasSequenceAssociations;

    private Long id;

    private Boolean isMerged;

    private Boolean isMergee;

    private Boolean isSubsumed;

    private Boolean isSubsumer;

    private java.util.Date lastGeneMapping;

    private java.util.Date lastRepeatMask;

    private java.util.Date lastSequenceAnalysis;

    private java.util.Date lastSequenceUpdate;

    private String name;

    private String numGenes;

    private String numProbeAlignments;

    private String numProbeSequences;

    private String numProbesToGenes;

    private String shortName;

    private Boolean troubled = false;

    private Boolean validated = false;

    private String taxon;

    private String technologyType;

    private boolean hasAnnotationFile;

    private String troubleDetails = "(Details of trouble not populated)";

    public Boolean getValidated() {
        return validated;
    }

    public String getTroubleDetails() {
        return troubleDetails;
    }

    public Platform() {
    }

    /**
     * Copies constructor from other Platform
     * 
     * @param otherBean, cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public Platform( Platform otherBean ) {
        this( otherBean.getName(), otherBean.getShortName(), otherBean.getDesignElementCount(), otherBean.getTaxon(),
                otherBean.getExpressionExperimentCount(), otherBean.getHasSequenceAssociations(), otherBean
                        .getHasBlatAssociations(), otherBean.getHasGeneAssociations(), otherBean.getId(), otherBean
                        .getColor(), otherBean.getNumProbeSequences(), otherBean.getNumProbeAlignments(), otherBean
                        .getNumProbesToGenes(), otherBean.getNumGenes(), otherBean.getDateCached(), otherBean
                        .getLastSequenceUpdate(), otherBean.getLastSequenceAnalysis(), otherBean.getLastGeneMapping(),
                otherBean.getIsSubsumed(), otherBean.getIsSubsumer(), otherBean.getIsMerged(), otherBean.getIsMergee(),
                otherBean.getLastRepeatMask(), otherBean.getTroubled(), otherBean.getValidated(), otherBean
                        .getDateCreated(), otherBean.getDescription(), otherBean.getTechnologyType() );
    }

    public Platform( String name, String shortName, Long designElementCount, String taxon,
            Long expressionExperimentCount, Boolean hasSequenceAssociations, Boolean hasBlatAssociations,
            Boolean hasGeneAssociations, Long id, String color, String numProbeSequences, String numProbeAlignments,
            String numProbesToGenes, String numGenes, String dateCached, java.util.Date lastSequenceUpdate,
            java.util.Date lastSequenceAnalysis, java.util.Date lastGeneMapping, Boolean isSubsumed,
            Boolean isSubsumer, Boolean isMerged, Boolean isMergee, java.util.Date lastRepeatMask,
            boolean troubleEvent, boolean validationEvent, java.util.Date dateCreated, String description,
            String technologyType ) {
        this.name = name;
        this.shortName = shortName;
        this.designElementCount = designElementCount;
        this.taxon = taxon;
        this.expressionExperimentCount = expressionExperimentCount;
        this.hasSequenceAssociations = hasSequenceAssociations;
        this.hasBlatAssociations = hasBlatAssociations;
        this.hasGeneAssociations = hasGeneAssociations;
        this.id = id;
        this.color = color;
        this.numProbeSequences = numProbeSequences;
        this.numProbeAlignments = numProbeAlignments;
        this.numProbesToGenes = numProbesToGenes;
        this.numGenes = numGenes;
        this.dateCached = dateCached;
        this.lastSequenceUpdate = lastSequenceUpdate;
        this.lastSequenceAnalysis = lastSequenceAnalysis;
        this.lastGeneMapping = lastGeneMapping;
        this.isSubsumed = isSubsumed;
        this.isSubsumer = isSubsumer;
        this.isMerged = isMerged;
        this.isMergee = isMergee;
        this.lastRepeatMask = lastRepeatMask;
        this.troubled = troubleEvent;
        this.validated = validationEvent;
        this.dateCreated = dateCreated;
        this.description = description;
        this.technologyType = technologyType;
    }

    /**
     * Copies all properties from the argument value object into this value object.
     */
    public void copy( Platform otherBean ) {
        if ( otherBean != null ) {
            this.setName( otherBean.getName() );
            this.setShortName( otherBean.getShortName() );
            this.setDesignElementCount( otherBean.getDesignElementCount() );
            this.setTaxon( otherBean.getTaxon() );
            this.setExpressionExperimentCount( otherBean.getExpressionExperimentCount() );
            this.setHasSequenceAssociations( otherBean.getHasSequenceAssociations() );
            this.setHasBlatAssociations( otherBean.getHasBlatAssociations() );
            this.setHasGeneAssociations( otherBean.getHasGeneAssociations() );
            this.setId( otherBean.getId() );
            this.setColor( otherBean.getColor() );
            this.setNumProbeSequences( otherBean.getNumProbeSequences() );
            this.setNumProbeAlignments( otherBean.getNumProbeAlignments() );
            this.setNumProbesToGenes( otherBean.getNumProbesToGenes() );
            this.setNumGenes( otherBean.getNumGenes() );
            this.setDateCached( otherBean.getDateCached() );
            this.setLastSequenceUpdate( otherBean.getLastSequenceUpdate() );
            this.setLastSequenceAnalysis( otherBean.getLastSequenceAnalysis() );
            this.setLastGeneMapping( otherBean.getLastGeneMapping() );
            this.setIsSubsumed( otherBean.getIsSubsumed() );
            this.setIsSubsumer( otherBean.getIsSubsumer() );
            this.setIsMerged( otherBean.getIsMerged() );
            this.setIsMergee( otherBean.getIsMergee() );
            this.setLastRepeatMask( otherBean.getLastRepeatMask() );
            this.setTroubled( otherBean.getTroubled() );
            this.setValidated( otherBean.getValidated() );
            this.setDateCreated( otherBean.getDateCreated() );
            this.setDescription( otherBean.getDescription() );
            this.setTechnologyType( otherBean.getTechnologyType() );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        Platform other = ( Platform ) obj;
        if ( id == null ) {
            if ( other.id != null ) return false;

            return id.equals( other.id );
        } else if ( !id.equals( other.id ) ) return false;
        if ( shortName == null ) {
            if ( other.shortName != null ) return false;
        } else if ( !shortName.equals( other.shortName ) ) return false;
        return true;
    }

    /**
         * 
         */
    public String getColor() {
        return this.color;
    }

    /**
         * 
         */
    public String getDateCached() {
        return this.dateCached;
    }

    /**
     * <p>
     * The date the Array Design was created
     * </p>
     */
    public java.util.Date getDateCreated() {
        return this.dateCreated;
    }

    /**  */
    public String getDescription() {
        return this.description;
    }

    /**
     * 
     */
    public Long getDesignElementCount() {
        return this.designElementCount;
    }

    /**  */
    public Long getExpressionExperimentCount() {
        return this.expressionExperimentCount;
    }

    /**
     * @return
     */
    public boolean getHasAnnotationFile() {
        return hasAnnotationFile;
    }

    /**   */
    public Boolean getHasBlatAssociations() {
        return this.hasBlatAssociations;
    }

    /**  */
    public Boolean getHasGeneAssociations() {
        return this.hasGeneAssociations;
    }

    /**  */
    public Boolean getHasSequenceAssociations() {
        return this.hasSequenceAssociations;
    }

    /**   */
    public Long getId() {
        return this.id;
    }

    /**
     * <p>
     * Indicates this array design is the merger of other array designs.
     * </p>
     */
    public Boolean getIsMerged() {
        return this.isMerged;
    }

    /**
     * <p>
     * Indicates that this array design has been merged into another.
     * </p>
     */
    public Boolean getIsMergee() {
        return this.isMergee;
    }

    /**
     * <p>
     * Indicate if this array design is subsumed by some other array design.
     * </p>
     */
    public Boolean getIsSubsumed() {
        return this.isSubsumed;
    }

    /**
     * <p>
     * Indicates if this array design subsumes some other array design(s)
     * </p>
     */
    public Boolean getIsSubsumer() {
        return this.isSubsumer;
    }

    /**
         * 
         */
    public java.util.Date getLastGeneMapping() {
        return this.lastGeneMapping;
    }

    /**
         * 
         */
    public java.util.Date getLastRepeatMask() {
        return this.lastRepeatMask;
    }

    /**
         * 
         */
    public java.util.Date getLastSequenceAnalysis() {
        return this.lastSequenceAnalysis;
    }

    /**
         * 
         */
    public java.util.Date getLastSequenceUpdate() {
        return this.lastSequenceUpdate;
    }

    /**
         * 
         */
    public String getName() {
        return this.name;
    }

    /**
     * <p>
     * The number of unique genes that this array design maps to.
     * </p>
     */
    public String getNumGenes() {
        return this.numGenes;
    }

    /**
     * <p>
     * The number of probes that have BLAT alignments.
     * </p>
     */
    public String getNumProbeAlignments() {
        return this.numProbeAlignments;
    }

    /**
     * <p>
     * The number of probes that map to bioSequences.
     * </p>
     */
    public String getNumProbeSequences() {
        return this.numProbeSequences;
    }

    /**
     * <p>
     * The number of probes that map to genes. This count includes probe-aligned regions, predicted genes, and known
     * genes.
     * </p>
     */
    public String getNumProbesToGenes() {
        return this.numProbesToGenes;
    }

    /**
         * 
         */
    public String getShortName() {
        return this.shortName;
    }

    /**
         * 
         */
    public String getTaxon() {
        return this.taxon;
    }

    /**
         * 
         */
    public String getTechnologyType() {
        return this.technologyType;
    }

    /**
     * @return the troubled
     */
    public Boolean getTroubled() {
        return troubled;
    }

    /**
     * The last uncleared TroubleEvent associated with this ArrayDesign.
     */
    public Boolean getTroubleEvent() {
        return this.troubled;
    }

    /**
     * The last uncleared TroubleEvent associated with this ArrayDesign.
     */
    public Boolean getValidationEvent() {
        return this.validated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        if ( id == null ) {
            result = prime * result + ( ( shortName == null ) ? 0 : shortName.hashCode() );
        }
        return result;
    }

    public void setColor( String color ) {
        this.color = color;
    }

    public void setDateCached( String dateCached ) {
        this.dateCached = dateCached;
    }

    public void setDateCreated( java.util.Date dateCreated ) {
        this.dateCreated = dateCreated;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void setDesignElementCount( Long designElementCount ) {
        this.designElementCount = designElementCount;
    }

    public void setExpressionExperimentCount( Long expressionExperimentCount ) {
        this.expressionExperimentCount = expressionExperimentCount;
    }

    public void setHasAnnotationFile( boolean b ) {
        this.hasAnnotationFile = b;
    }

    public void setHasBlatAssociations( Boolean hasBlatAssociations ) {
        this.hasBlatAssociations = hasBlatAssociations;
    }

    public void setHasGeneAssociations( Boolean hasGeneAssociations ) {
        this.hasGeneAssociations = hasGeneAssociations;
    }

    public void setHasSequenceAssociations( Boolean hasSequenceAssociations ) {
        this.hasSequenceAssociations = hasSequenceAssociations;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public void setIsMerged( Boolean isMerged ) {
        this.isMerged = isMerged;
    }

    public void setIsMergee( Boolean isMergee ) {
        this.isMergee = isMergee;
    }

    public void setIsSubsumed( Boolean isSubsumed ) {
        this.isSubsumed = isSubsumed;
    }

    public void setIsSubsumer( Boolean isSubsumer ) {
        this.isSubsumer = isSubsumer;
    }

    public void setLastGeneMapping( java.util.Date lastGeneMapping ) {
        this.lastGeneMapping = lastGeneMapping;
    }

    public void setLastRepeatMask( java.util.Date lastRepeatMask ) {
        this.lastRepeatMask = lastRepeatMask;
    }

    public void setLastSequenceAnalysis( java.util.Date lastSequenceAnalysis ) {
        this.lastSequenceAnalysis = lastSequenceAnalysis;
    }

    public void setLastSequenceUpdate( java.util.Date lastSequenceUpdate ) {
        this.lastSequenceUpdate = lastSequenceUpdate;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setNumGenes( String numGenes ) {
        this.numGenes = numGenes;
    }

    public void setNumProbeAlignments( String numProbeAlignments ) {
        this.numProbeAlignments = numProbeAlignments;
    }

    public void setNumProbeSequences( String numProbeSequences ) {
        this.numProbeSequences = numProbeSequences;
    }

    public void setNumProbesToGenes( String numProbesToGenes ) {
        this.numProbesToGenes = numProbesToGenes;
    }

    public void setShortName( String shortName ) {
        this.shortName = shortName;
    }

    public void setTaxon( String taxon ) {
        this.taxon = taxon;
    }

    public void setTechnologyType( String technologyType ) {
        this.technologyType = technologyType;
    }

    /**
     * @param troubled the troubled to set
     */
    public void setTroubled( Boolean troubled ) {
        this.troubled = troubled;
    }

    public void setTroubleDetails( String troubleEvent ) {
        this.troubleDetails = troubleEvent;
    }

    public void setValidated( Boolean validated ) {
        this.validated = validated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getShortName();
    }

    @Override
    public int compareTo( Platform arg0 ) {

        if ( arg0.getDateCreated() == null || this.getDateCreated() == null ) return 0;

        return arg0.getDateCreated().compareTo( this.getDateCreated() );

    }

}