package org.graphstream.ui.j2dviewer.renderer

import java.awt.Graphics2D
import org.graphstream.ui2.graphicGraph.{GraphicElement, StyleGroup}
import org.graphstream.ui.j2dviewer.util.Camera
import org.graphstream.ui.j2dviewer.renderer.shape._

class NodeRenderer( styleGroup:StyleGroup ) extends StyleRenderer( styleGroup ) {
	protected var shape:AreaShape = null
 
	protected def setupRenderingPass( g:Graphics2D, camera:Camera, forShadow:Boolean ) {
		shape = chooseShape
	}
	
	protected def pushStyle( g:Graphics2D, camera:Camera, forShadow:Boolean ) {
		val size = group.getSize
		shape.configure( g, group, camera, null )
		shape.size( group, camera )
	}
	
	protected def pushDynStyle( g:Graphics2D, camera:Camera, element:GraphicElement ) {
		val size = group.getSize
		shape.configure( g, group, camera, element )
		shape.size( group, camera )
	}
	
	protected def renderElement( g:Graphics2D, camera:Camera, element:GraphicElement ) {
		shape.text = element.label
		shape.position( element.getX, element.getY )
		shape.render( g, camera )
	}
	
	protected def renderShadow( g:Graphics2D, camera:Camera, element:GraphicElement ) {
		shape.position( element.getX, element.getY )
		shape.renderShadow( g, camera )
	}
 
	protected def elementInvisible( g:Graphics2D, camera:Camera, element:GraphicElement ) {
		// NOP
	}
 
	protected def chooseShape():AreaShape = {
		import org.graphstream.ui2.graphicGraph.stylesheet.StyleConstants.Shape._
		group.getShape match {
			case CIRCLE         => new CircleShape 
		  	case BOX            => new SquareShape
		  	case ROUNDED_BOX    => new RoundedSquareShape
		  	// ------------------------------------------
		  	case DIAMOND        => Console.err.printf( "** SORRY diamond shape not yet implemented **%n" );      new CircleShape
		  	case POLYGON        => Console.err.printf( "** SORRY polygon shape not yet implemented **%n" );      new CircleShape
		    case TRIANGLE       => Console.err.printf( "** SORRY triangle shape not yet implemented **%n" );     new CircleShape
		    case CROSS          => Console.err.printf( "** SORRY cross shape not yet implemented **%n" );        new CircleShape
		    case TEXT_BOX       => Console.err.printf( "** SORRY text-box shape not yet implemented **%n" );     new SquareShape
		    case TEXT_PARAGRAPH => Console.err.printf( "** SORRY text-para shape not yet implemented **%n" );    new SquareShape
		    case TEXT_CIRCLE    => Console.err.printf( "** SORRY text-circle shape not yet implemented **%n" );  new CircleShape
		    case TEXT_DIAMOND   => Console.err.printf( "** SORRY text-diamond shape not yet implemented **%n" ); new CircleShape
		    case PIE_CHART      => Console.err.printf( "** SORRY pie-chart shape not yet implemented **%n" );    new CircleShape
		    case ARROW          => Console.err.printf( "** SORRY arrow shape not yet implemented **%n" );        new CircleShape
		    case IMAGES         => Console.err.printf( "** SORRY images shape not yet implemented **%n" );       new SquareShape 
		    case JCOMPONENT     => throw new RuntimeException( "WTF, jcomponent should have its own renderer" )
		    case x              => throw new RuntimeException( "%s shape cannot be set for nodes".format( x.toString ) )
		}
	}
}

object NodeRenderer {
	def apply( style:StyleGroup, mainRenderer:J2DGraphRenderer ):StyleRenderer = {
		if( style.getShape == org.graphstream.ui2.graphicGraph.stylesheet.StyleConstants.Shape.JCOMPONENT )
		     new JComponentRenderer( style, mainRenderer )
		else new NodeRenderer( style )
	}
}